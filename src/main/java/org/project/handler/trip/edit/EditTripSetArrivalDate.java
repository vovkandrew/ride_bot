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
import static org.project.util.constants.Constants.DATE_FORMAT;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.DATE;
import static org.project.util.enums.HandlerName.*;
import static org.project.util.enums.Status.CREATED;

@Component
public class EditTripSetArrivalDate extends UpdateHandler {
    private final TripService tripService;

    public EditTripSetArrivalDate(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_ARRIVAL_DATE) || super.isApplicable(phaseOptional, update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        updateUserPhase(userPhase, DRIVER_TRIP_EDITING_ARRIVAL_DATE);
        Trip trip;

        if (isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_ARRIVAL_DATE)) {
            trip = tripService.getTrip(getCallbackQueryIdParamFromUpdate(update));

            tripService.updateAllEditingTrips(trip);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendEditableMessage(userId, DRIVER_TRIP_ENTER_ARRIVAL_DATE);

            return;
        }

        trip = tripService.getFirstEditingTrip(userId);

        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, DATE) && trip.verifyArrivalDate(userInput)) {
            trip.setStatus(CREATED);

            tripService.updateTripArrivalDate(trip, userInput);

            editMessage(userId, format(DRIVER_TRIP_ARRIVAL_DATE_PROVIDED, trip.getArrivalDate().format(ofPattern(DATE_FORMAT))));

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendRemovableMessage(userId, format(TRIP_DETAILS, trip.getFormattedData()),
                    getDriverTripDetailsKeyboard(trip.getId(), DRIVER_TRIP_DETAILS_LESS));

            updateUserPhase(userPhase, DRIVER_TRIP_DETAILS);

            return;
        }

        sendRemovableMessage(userId, DRIVER_TRIP_WRONG_ARRIVAL_DATE);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIP_EDITING_ARRIVAL_DATE);
    }
}
