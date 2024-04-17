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
import static org.project.util.constants.Constants.DATE_FORMAT;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.DATE_PATTERN;
import static org.project.util.enums.HandlerName.DRIVER_TRIP_EDITING_DEPARTURE_DATE;
import static org.project.util.enums.Status.CREATED;

@Component
public class EditTripSetDepartureDate extends EditTripDetails {
    public EditTripSetDepartureDate(TripService tripService, DriverService driverService,
                                    BookingService bookingService) {
        super(tripService, driverService, bookingService);
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_DEPARTURE_DATE) || super.isApplicable(phaseOptional, update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);
        updateUserPhase(userPhase, DRIVER_TRIP_EDITING_DEPARTURE_DATE);
        Trip trip;

        if (isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_DEPARTURE_DATE)) {
            trip = getTripService().getTrip(getCallbackQueryIdParamFromUpdate(update));

            getTripService().updateAllEditingTrips(trip);

            deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

            sendEditableMessage(telegramUserId, DRIVER_TRIP_ENTER_DEPARTURE_DATE);

            return;
        }

        trip = getTripService().getFirstEditingTrip(telegramUserId);

        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, DATE_PATTERN) && trip.verifyDepartureDate(userInput)) {
            trip.setStatus(CREATED);

            getTripService().updateTripDepartureDate(trip, userInput);

            editMessage(telegramUserId, format(DRIVER_TRIP_DEPARTURE_DATE_PROVIDED,
                    trip.getDepartureDate().format(ofPattern(DATE_FORMAT))));

            sendDriverTripDetailsAndUpdateUserPhase(telegramUserId, trip, userPhase);

            return;
        }

        sendRemovableMessage(telegramUserId, DRIVER_TRIP_WRONG_DEPARTURE_DATE);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIP_EDITING_DEPARTURE_DATE);
    }
}
