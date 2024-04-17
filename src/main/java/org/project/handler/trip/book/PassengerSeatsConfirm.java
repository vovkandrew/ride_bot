package org.project.handler.trip.book;

import org.project.handler.UpdateHandler;
import org.project.model.*;
import org.project.service.*;
import org.project.util.UpdateHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.*;
import static org.project.util.UpdateHelper.getTelegramUserIdFromUpdate;
import static org.project.util.UpdateHelper.isUpdateContainsHandler;
import static org.project.util.constants.Buttons.BACK_BUTTON;
import static org.project.util.constants.Constants.DEFAULT_OFFSET;
import static org.project.util.constants.Constants.DEFAULT_TRIP_LIMIT;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.CAR_SEATS_NUMBER_PATTERN;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;

@Component
public class PassengerSeatsConfirm extends UpdateHandler {
	private final RouteService routeService;
	private final TripService tripService;
	private final BookingService bookingService;
	private final TelegramUserService telegramUserService;
	private final PaymentService paymentService;
	@Value("${passenger.agreement.file.path}")
	private String passengerAgreementFilePath;
	private String telegramFileId;

	public PassengerSeatsConfirm(RouteService routeService, TripService tripService, BookingService bookingService,
								 TelegramUserService telegramUserService, PaymentService paymentService) {
		this.routeService = routeService;
		this.tripService = tripService;
		this.bookingService = bookingService;
		this.telegramUserService = telegramUserService;
		this.paymentService = paymentService;
	}

	@Override
	public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
		return super.isApplicable(phaseOptional, update) || isUpdateContainsHandler(update, handlerPhase);
	}

	@Override
	public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
		long telegramUserId = getTelegramUserIdFromUpdate(update);

		deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);
		updateUserPhase(userPhase, handlerPhase);

		String userInput = UpdateHelper.getUserInputFromUpdate(update);

		Booking booking = bookingService.getNewBooking(telegramUserId);

		Trip trip = booking.getTrip();

		if (isUserInputMatchesPattern(userInput, CAR_SEATS_NUMBER_PATTERN)) {

			int seatsInput = Integer.parseInt(userInput);

			int availableSeats = bookingService.getAvailableSeats(trip);

			if (availableSeats == 0) {
				sendRemovableMessage(telegramUserId, NO_EMPTY_SEATS_LEFT);

				Route route = routeService.getNewPassengerRoute(telegramUserId);
				Page<Trip> trips = tripService.findAllCreatedNonDriverTrips(route,
						of(DEFAULT_OFFSET, DEFAULT_TRIP_LIMIT));

				sendRemovableMessage(telegramUserId, format(FIND_TRIP_CHOOSE_TRIPS, route.getFormattedData()),
						getAvailableTripsForPassengerKeyboard(trips, FIND_TRIP_MENU_NEXT, FIND_TRIP_MENU_DETAILS,
								FIND_TRIP_CITY_TO, BACK_BUTTON));

				return;
			}

			if (seatsInput > availableSeats) {
				sendRemovableMessage(telegramUserId, PASSENGER_SEATS_NUMBER_EXCEEDED);

				return;
			}

			Optional<CreatePaymentLinkResponse> createPaymentLinkResponse =
					paymentService.generatePaymentLink(trip, seatsInput, booking.getId());

			if (createPaymentLinkResponse.isEmpty()) {
				sendRemovableMessage(telegramUserId, CANT_CREATE_PAYMENT_LINK, getPassengerMainMenuKeyboard());

				return;
			}

			bookingService.updateNumberOfBookedSeats(booking, seatsInput);

			sendRemovableMessage(telegramUserId, format(PASSENGER_ENTER_SEATS_PROVIDED, seatsInput));

			InputFile agreementFile = Optional.ofNullable(telegramFileId).isPresent() ? new InputFile(telegramFileId)
					: new InputFile(new File(passengerAgreementFilePath));

			Message removable = getWebhookBot().execute(SendDocument.builder().chatId(telegramUserId)
					.replyMarkup(getPaymentConfirmationKeyboard(DECLINE_BOOKING_SEATS,
							createPaymentLinkResponse.get().getPageUrl())).document(agreementFile).build());

			telegramFileId = removable.getDocument().getFileId();

			getUserMessageService().createRemovableMessage(telegramUserId, removable.getMessageId());

			return;
		}

		sendRemovableMessage(telegramUserId, SEATS_NUMBER_INVALID);
	}

	@Override
	public void initHandler() {
		handlerPhase = getPhaseService().getPhaseByHandlerName(PASSENGER_SEATS_CONFIRM);
	}
}
