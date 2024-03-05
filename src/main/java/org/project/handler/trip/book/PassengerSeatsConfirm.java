package org.project.handler.trip.book;

import org.project.handler.UpdateHandler;
import org.project.model.*;
import org.project.service.BookingService;
import org.project.service.RouteService;
import org.project.service.TelegramUserService;
import org.project.service.TripService;
import org.project.util.UpdateHelper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.getAvailableTripsForPassengerKeyboard;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.UpdateHelper.isUpdateContainsHandler;
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

	public PassengerSeatsConfirm(RouteService routeService, TripService tripService, BookingService bookingService,
	                             TelegramUserService telegramUserService) {
		this.routeService = routeService;
		this.tripService = tripService;
		this.bookingService = bookingService;
		this.telegramUserService = telegramUserService;
	}

	@Override
	public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
		return super.isApplicable(phaseOptional, update) || isUpdateContainsHandler(update, handlerPhase);
	}

	@Override
	public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
		long userId = getUserIdFromUpdate(update);

		deleteRemovableMessagesAndEraseAllFromRepo(userId);
		updateUserPhase(userPhase, handlerPhase);

		String userInput = UpdateHelper.getUserInputFromUpdate(update);

		if (isUserInputMatchesPattern(userInput, CAR_SEATS_NUMBER_PATTERN)) {
			Booking booking = bookingService.getNewBooking(telegramUserService.getTelegramUser(userId).getId());

			Trip trip = booking.getTrip();

			int seatsInput = Integer.parseInt(userInput);

			int availableSeats = bookingService.getAvailableSeats(trip);

			if (availableSeats == 0) {
				sendRemovableMessage(userId, NO_EMPTY_SEATS_LEFT);

				Route route = routeService.getNewPassengerRoute(userId);
				Page<Trip> trips = tripService.findAllCreatedNonDriverTrips(route,
						of(DEFAULT_OFFSET, DEFAULT_TRIP_LIMIT));

				sendRemovableMessage(userId, format(FIND_TRIP_CHOOSE_TRIPS, route.getFormattedData()),
						getAvailableTripsForPassengerKeyboard(trips, FIND_TRIP_MENU_NEXT, FIND_TRIP_MENU_DETAILS));

				return;
			}

			if (seatsInput > availableSeats) {
				sendRemovableMessage(userId, PASSENGER_SEATS_TOO_MUCH);

				return;
			}

			sendRemovableMessage(userId, format(PASSENGER_ENTER_SEATS_PROVIDED, seatsInput));

			return;
		}
		sendRemovableMessage(userId, SEATS_NUMBER_INVALID);
	}

	@Override
	public void initHandler() {
		handlerPhase = getPhaseService().getPhaseByHandlerName(PASSENGER_SEATS_CONFIRM);
	}

}