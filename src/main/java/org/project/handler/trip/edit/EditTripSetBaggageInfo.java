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
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.GENERAL_MESSAGE_PATTERN;
import static org.project.util.enums.HandlerName.DRIVER_TRIP_EDITING_BAGGAGE_INFO;
import static org.project.util.enums.Status.CREATED;

@Component
public class EditTripSetBaggageInfo extends EditTripDetails {
    public EditTripSetBaggageInfo(TripService tripService, DriverService driverService, BookingService bookingService) {
        super(tripService, driverService, bookingService);
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_BAGGAGE_INFO)
                || super.isApplicable(phaseOptional, update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);
        updateUserPhase(userPhase, DRIVER_TRIP_EDITING_BAGGAGE_INFO);
        Trip trip;

        if (isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_BAGGAGE_INFO)) {
            trip = getTripService().getTrip(getCallbackQueryIdParamFromUpdate(update));

            getTripService().updateAllEditingTrips(trip);

            deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

            sendEditableMessage(telegramUserId, DRIVER_TRIP_ENTER_BAGGAGE_INFO);

            return;
        }

        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, GENERAL_MESSAGE_PATTERN)) {
            trip = getTripService().getFirstEditingTrip(telegramUserId);

            trip.setStatus(CREATED);

            getTripService().updateTripBaggageInfo(trip, userInput);

            editMessage(telegramUserId, format(DRIVER_TRIP_BAGGAGE_INFO_PROVIDED, trip.getBaggageInfo()));

            sendDriverTripDetailsAndUpdateUserPhase(telegramUserId, trip, userPhase);

            return;
        }

        sendRemovableMessage(telegramUserId, DRIVER_TRIP_WRONG_BAGGAGE_INFO);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIP_EDITING_BAGGAGE_INFO);
    }
}
