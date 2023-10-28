package org.project.handler.trip;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.TripService;
import org.project.util.enums.HandlerName;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.getDriverTripDetailsKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Messages.TRIP_DETAILS;
import static org.project.util.enums.HandlerName.*;

@Component
public class DriverTripDetails extends UpdateHandler {
    private final TripService tripService;

    public DriverTripDetails(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandler(update, DRIVER_TRIP_DETAILS);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        updateUserPhase(userPhase, DRIVER_TRIP_DETAILS);

        int dataParam = getCallbackQueryIdParamFromUpdate(update);

        HandlerName handlerName = isUpdateContainsHandler(update, DRIVER_TRIP_DETAILS_LESS) ? DRIVER_TRIP_DETAILS_MORE
                : DRIVER_TRIP_DETAILS_LESS;

        Trip trip = tripService.getTrip(dataParam);

        sendRemovableMessage(userId, format(TRIP_DETAILS, trip.getFormattedData()),
                getDriverTripDetailsKeyboard(trip.getId(), handlerName));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIP_DETAILS);
    }
}
