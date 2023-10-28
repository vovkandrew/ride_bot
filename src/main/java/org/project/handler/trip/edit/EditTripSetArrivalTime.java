package org.project.handler.trip.edit;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.TripService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.project.util.Keyboards.getDriverTripDetailsKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Constants.TIME_FORMAT;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.TIME;
import static org.project.util.enums.HandlerName.*;
import static org.project.util.enums.Status.CREATED;

@Component
public class EditTripSetArrivalTime extends UpdateHandler {
    private final TripService tripService;

    public EditTripSetArrivalTime(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_ARRIVAL_TIME) || super.isApplicable(phaseOptional, update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        updateUserPhase(userPhase, DRIVER_TRIP_EDITING_ARRIVAL_TIME);
        Trip trip;

        if (isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_ARRIVAL_TIME)) {
            trip = tripService.getTrip(getCallbackQueryIdParamFromUpdate(update));

            tripService.updateAllEditingTrips(trip);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendEditableMessage(userId, DRIVER_TRIP_ENTER_ARRIVAL_TIME);

            return;
        }

        trip = tripService.getFirstEditingTrip(userId);

        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, TIME) && trip.verifyArrivalTime(userInput)) {
            trip.setStatus(CREATED);
            tripService.updateTripArrivalTime(trip, userInput);

            editMessage(userId, format(DRIVER_TRIP_ARRIVAL_TIME_PROVIDED, trip.getArrivalTime().format(ofPattern(TIME_FORMAT))));

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendRemovableMessage(userId, format(TRIP_DETAILS, trip.getFormattedData()),
                    getDriverTripDetailsKeyboard(trip.getId(), DRIVER_TRIP_DETAILS_LESS));

            updateUserPhase(userPhase, DRIVER_TRIP_DETAILS);

            return;
        }

        sendRemovableMessage(userId, DRIVER_TRIP_WRONG_ARRIVAL_TIME);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIP_EDITING_ARRIVAL_TIME);
    }
}
