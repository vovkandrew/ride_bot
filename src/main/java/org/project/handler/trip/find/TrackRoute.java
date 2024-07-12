package org.project.handler.trip.find;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.Route;
import org.project.model.UserPhase;
import org.project.service.RouteService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.getPassengerMainMenuKeyboard;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.PASSENGER_MENU;
import static org.project.util.enums.HandlerName.TRACK_TRIP;
import static org.project.util.enums.Status.CREATED;

@Component
public class TrackRoute extends UpdateHandler {
    private RouteService routeService;

    public TrackRoute(RouteService routeService) {
        this.routeService = routeService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return super.isApplicable(phaseOptional, update) || isUpdateContainsHandlerPhase(update);
    }
    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        Route route = routeService.getNewRoute(userId);

        if (routeService.isPassengerRouteExist(route)){
            sendMessage(userId, format(TRACKING_ROUTE_ALREADY_EXISTS, route.getSimplifiedRoute()));
        } else {
            route = routeService.updateRouteStatus(route, CREATED);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendMessage(userId, format(START_TRACKING_ROUTE, route.getSimplifiedRoute()));
        }

        updateUserPhase(userPhase, PASSENGER_MENU);

        sendRemovableMessage(userId, GREETING_PASSENGER, getPassengerMainMenuKeyboard());
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(TRACK_TRIP);
    }
}
