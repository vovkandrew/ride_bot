package org.project.handler.trip.edit;

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

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Constants.TIME_FORMAT;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.TIME_PATTERN;
import static org.project.util.enums.HandlerName.DRIVER_TRIP_EDITING_DEPARTURE_TIME;
import static org.project.util.enums.Status.CREATED;

@Component
public class EditTripSetDepartureTime extends EditTripDetails {
    public EditTripSetDepartureTime(TripService tripService, DriverService driverService,
                                    BookingService bookingService) {
        super(tripService, driverService, bookingService);
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_DEPARTURE_TIME)
                || super.isApplicable(phaseOptional, update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        updateUserPhase(userPhase, DRIVER_TRIP_EDITING_DEPARTURE_TIME);
        Trip trip;

        if (isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_DEPARTURE_TIME)) {
            trip = getTripService().getTrip(getCallbackQueryIdParamFromUpdate(update));

            getTripService().updateAllEditingTrips(trip);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendEditableMessage(userId, DRIVER_TRIP_ENTER_DEPARTURE_TIME);

            return;
        }

        String userInput = getUserInputFromUpdate(update);

        trip = getTripService().getFirstEditingTrip(userId);

        if (isUserInputMatchesPattern(userInput, TIME_PATTERN) && trip.verifyDepartureTime(userInput)) {

            trip.setStatus(CREATED);

            getTripService().updateTripDepartureTime(trip, userInput);

            editMessage(userId, format(DRIVER_TRIP_DEPARTURE_TIME_PROVIDED,
                    trip.getDepartureTime().format(ofPattern(TIME_FORMAT))));

            sendDriverTripDetailsAndUpdateUserPhase(userId, trip, userPhase);

            return;
        }

        sendRemovableMessage(userId, DRIVER_TRIP_WRONG_DEPARTURE_TIME);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIP_EDITING_DEPARTURE_TIME);
    }
}
