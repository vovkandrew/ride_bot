package org.project.handler.trip.edit.add.passenger;

import org.project.handler.trip.edit.EditTripDetails;
import org.project.model.Driver;
import org.project.model.Phase;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.BookingService;
import org.project.service.DriverService;
import org.project.service.TripService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.DRIVER_TRIP_ADDING_PASSENGER;
import static org.project.util.enums.HandlerName.DRIVER_TRIP_ADDING_PASSENGER_NAME;

@Component
public class EditTripAddPassenger extends EditTripDetails {
    public EditTripAddPassenger(TripService tripService, BookingService bookingService,
                                DriverService driverService) {
        super(tripService, driverService, bookingService);
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandler(update, DRIVER_TRIP_ADDING_PASSENGER);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);

        updateUserPhase(userPhase, DRIVER_TRIP_ADDING_PASSENGER);

        Trip trip = getTripService().getTrip(getCallbackQueryIdParamFromUpdate(update));

        Driver driver = getDriverService().getDriver(telegramUserId);

        if (driver.getSeatsNumber() - getBookingService().getNumberOfBookedSeats(trip) > 0) {
            sendMessage(telegramUserId, EDIT_TRIP_ADD_PASSENGER_SET_NAME);

            updateUserPhase(userPhase, DRIVER_TRIP_ADDING_PASSENGER_NAME);
        } else {
            sendMessage(telegramUserId, NO_EMPTY_SEATS_LEFT);

            sendDriverTripDetailsAndUpdateUserPhase(telegramUserId, trip, userPhase);
        }
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIP_ADDING_PASSENGER);
    }
}
