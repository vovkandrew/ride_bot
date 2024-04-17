package org.project.handler.trip.book;

import org.project.handler.UpdateHandler;
import org.project.model.*;
import org.project.service.BookingService;
import org.project.service.RouteService;
import org.project.service.TelegramUserService;
import org.project.service.TripService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.getAvailableTripsForPassengerKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Buttons.BACK_BUTTON;
import static org.project.util.constants.Constants.DEFAULT_OFFSET;
import static org.project.util.constants.Constants.DEFAULT_TRIP_LIMIT;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.*;
import static org.project.util.enums.Status.NEW;
import static org.springframework.data.domain.PageRequest.of;

@Component
public class PassengerReserveSeats extends UpdateHandler {
	private final RouteService routeService;
	private final TripService tripService;
	private final BookingService bookingService;
	private final TelegramUserService telegramUserService;

	public PassengerReserveSeats(RouteService routeService, TripService tripService, BookingService bookingService,
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
		long telegramUserId = getTelegramUserIdFromUpdate(update);

		long tripId = getCallbackQueryIdParamFromUpdate(update);

		TelegramUser telegramUser = telegramUserService.getTelegramUser(telegramUserId);

		Trip trip = tripService.getTrip(tripId);

		deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

		if (bookingService.hasNewBooking(telegramUser)) {
			sendEditableMessage(telegramUserId, CHECK_AVAILABLE_SEATS);

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

			updateUserPhase(userPhase, PASSENGER_SEATS_CONFIRM);

			bookingService.updateBookingTrip(bookingService.getNewBooking(telegramUserId), trip);

			sendRemovableMessage(telegramUserId,
					joinMessages(format(PASSENGER_SEATS_FOUND, availableSeats), PASSENGER_ENTER_SEATS));
		} else {
			sendEditableMessage(telegramUserId, CHECK_AVAILABLE_SEATS);

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

			updateUserPhase(userPhase, PASSENGER_SEATS_CONFIRM);

			bookingService.updateBookingTrip(Booking.builder().telegramUser(telegramUser).status(NEW).build(), trip);

			sendRemovableMessage(telegramUserId,
					joinMessages(format(PASSENGER_SEATS_FOUND, availableSeats), PASSENGER_ENTER_SEATS));
		}
	}

	@Override
	public void initHandler() {
		handlerPhase = getPhaseService().getPhaseByHandlerName(PASSENGER_RESERVE_SEATS);
	}

}
