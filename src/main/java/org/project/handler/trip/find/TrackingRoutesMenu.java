package org.project.handler.trip.find;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.UserPhase;
import org.project.service.RouteService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.*;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Constants.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Component
public class TrackingRoutesMenu extends UpdateHandler {
    private final RouteService routeService;

    public TrackingRoutesMenu(RouteService routeService) {
        this.routeService = routeService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return super.isApplicable(phaseOptional, update)
                || isUpdateContainsAnyHandler(update, handlerPhase.getHandlerName(), TRACKING_ROUTES_NEXT);
    }
    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        updateUserPhase(userPhase, handlerPhase);

        if (isMessageSentInsteadOfButtonClick(update)) {
            return;
        }

        int page = getOffsetParamFromUpdateByHandler(update, TRACKING_ROUTES_NEXT);
        PageRequest pageRequest = of(page, DEFAULT_ROUTE_LIMIT, ASC, DEFAULT_ID_FIELD);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);
        updateUserPhase(userPhase, TRACK_ROUTE_FIND_TRIP);

        sendRemovableMessage(userId, TRACKING_ROUTES_MENU, getAvailablePassengerTrackingRoutesKeyboard(
                routeService.getAllCreatedPickedPassengerRoutes(pageRequest, userId),
                TRACKING_ROUTES_NEXT, TRACK_ROUTE_FIND_TRIP_FIRST));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(TRACKING_ROUTES);
    }


}
