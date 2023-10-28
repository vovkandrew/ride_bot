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
import static org.project.util.Keyboards.getDriverTripDetailsKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.GENERAL_MESSAGE;
import static org.project.util.enums.HandlerName.*;
import static org.project.util.enums.Status.CREATED;

@Component
public class EditTripSetOtherInfo extends UpdateHandler {
    private final TripService tripService;

    public EditTripSetOtherInfo(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_OTHER_INFO) || super.isApplicable(phaseOptional, update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        updateUserPhase(userPhase, DRIVER_TRIP_EDITING_OTHER_INFO);
        Trip trip;

        if (isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_OTHER_INFO)) {
            trip = tripService.getTrip(getCallbackQueryIdParamFromUpdate(update));

            tripService.updateAllEditingTrips(trip);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendEditableMessage(userId, DRIVER_TRIP_EDIT_OTHER_INFO);

            return;
        }

        trip = tripService.getFirstEditingTrip(userId);

        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, GENERAL_MESSAGE)) {
            trip.setStatus(CREATED);

            tripService.updateTripOtherInfo(trip, userInput);

            editMessage(userId, format(DRIVER_TRIP_OTHER_INFO_PROVIDED, trip.getOtherInfo()));

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendRemovableMessage(userId, format(TRIP_DETAILS, trip.getFormattedData()),
                    getDriverTripDetailsKeyboard(trip.getId(), DRIVER_TRIP_DETAILS_LESS));

            updateUserPhase(userPhase, DRIVER_TRIP_DETAILS);

            return;
        }

        sendRemovableMessage(userId, DRIVER_TRIP_WRONG_OTHER_INFO);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIP_EDITING_OTHER_INFO);
    }
}
