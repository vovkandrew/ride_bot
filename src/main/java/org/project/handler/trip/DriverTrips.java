package org.project.handler.trip;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.UserPhase;
import org.project.service.TripService;
import org.project.util.UpdateHelper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static org.project.util.Keyboards.getDriverTripsMenuKeyboard;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.constants.Constants.DEFAULT_ID_FIELD;
import static org.project.util.constants.Constants.DEFAULT_TRIP_LIMIT;
import static org.project.util.constants.Messages.DRIVER_TRIPS_MENU;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Component
public class DriverTrips extends UpdateHandler {
    private final TripService tripService;

    public DriverTrips(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandlerPhase(update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        updateUserPhase(userPhase, handlerPhase);

        int page = UpdateHelper.getOffsetParamFromUpdateByHandler(update, DRIVER_TRIPS_NEXT);

        PageRequest pageRequest = of(page, DEFAULT_TRIP_LIMIT, ASC, DEFAULT_ID_FIELD);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        sendRemovableMessage(userId, DRIVER_TRIPS_MENU, getDriverTripsMenuKeyboard(
                tripService.findAllCreatedNonDriverTrips(userId, pageRequest), DRIVER_TRIPS_NEXT, DRIVER_TRIP_DETAILS));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIPS);
    }
}
