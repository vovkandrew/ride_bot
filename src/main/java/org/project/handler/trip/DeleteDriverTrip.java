package org.project.handler.trip;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.TripService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.*;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Constants.DEFAULT_ID_FIELD;
import static org.project.util.constants.Constants.DEFAULT_TRIP_LIMIT;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Component
public class DeleteDriverTrip extends UpdateHandler {
    private final TripService tripService;

    public DeleteDriverTrip(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsAnyHandler(update, DRIVER_TRIP_DELETION, DELETE_DRIVER_TRIP_CONFIRMED, DELETE_DRIVER_TRIP_DECLINED)
                || super.isApplicable(phaseOptional, update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);
        int tripId = getCallbackQueryIdParamFromUpdate(update);
        Trip trip = tripService.getTrip(tripId);

        deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

        if (isUpdateCallbackEqualsHandler(update, DELETE_DRIVER_TRIP_CONFIRMED)) {
            tripService.deleteTrip(tripId);

            sendMessage(telegramUserId, DRIVER_TRIP_DELETED);

            PageRequest pageRequest = of(0, DEFAULT_TRIP_LIMIT, ASC, DEFAULT_ID_FIELD);

            sendRemovableMessage(telegramUserId, DRIVER_TRIPS_MENU, getDriverTripsMenuKeyboard(
                    tripService.findAllCreatedNonDriverTrips(telegramUserId, pageRequest),
                    DRIVER_TRIPS_NEXT, DRIVER_TRIP_DETAILS));

            return;
        }

        if (isUpdateCallbackEqualsHandler(update, DRIVER_TRIP_DELETION)) {
            sendRemovableMessage(telegramUserId, format(DELETE_DRIVER_TRIP, trip.getRoute().getSimplifiedRoute()),
                    getConfirmationKeyboard(joinHandlerAndParam(DELETE_DRIVER_TRIP_CONFIRMED, tripId),
                    joinHandlerAndParam(DELETE_DRIVER_TRIP_DECLINED, tripId)));

            return;
        }

        sendRemovableMessage(telegramUserId, format(TRIP_DETAILS, trip.getFormattedData()),
                getDriverTripDetailsKeyboard(trip.getId(), DRIVER_TRIP_DETAILS_LESS));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIP_DELETION);
    }
}
