package org.project.handler.trip.create;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.RouteService;
import org.project.service.TripService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.getContinueCreateDriverTripKeyboard;
import static org.project.util.Keyboards.getDriverRoutesKeyboard;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.UpdateHelper.isUpdateContainsHandler;
import static org.project.util.constants.Constants.*;
import static org.project.util.constants.Messages.CONTINUE_CREATE_DRIVER_TRIP;
import static org.project.util.constants.Messages.TRIP_CREATING_CHOOSE_ROUTE;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Service
public class CreateTripStart extends UpdateHandler {
    private final TripService tripService;
    private final RouteService routeService;

    public CreateTripStart(TripService tripService, RouteService routeService) {
        this.tripService = tripService;
        this.routeService = routeService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandlerPhase(update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        updateUserPhase(userPhase, handlerPhase);

        Optional<Trip> unfinished = tripService.findNewTrip(userId);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        if (unfinished.isPresent()) {
            Trip trip = unfinished.get();

            sendRemovableMessage(userId, format(CONTINUE_CREATE_DRIVER_TRIP, trip.getFormattedData()),
                    getContinueCreateDriverTripKeyboard());

            return;
        }

        PageRequest pageRequest = of(DEFAULT_OFFSET, DEFAULT_ROUTE_LIMIT, ASC, DEFAULT_ID_FIELD);

        sendEditableMessage(userId, TRIP_CREATING_CHOOSE_ROUTE, getDriverRoutesKeyboard(
                routeService.getAllCreatedRoutes(pageRequest, userId), CREATE_TRIP_CHOOSE_ROUTE_NEXT, CREATE_TRIP_CHOOSE_ROUTE));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(CREATE_DRIVER_TRIP);
    }
}
