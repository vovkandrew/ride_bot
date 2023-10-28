package org.project.handler.route;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.Route;
import org.project.model.UserPhase;
import org.project.service.RouteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static org.project.util.Keyboards.getDriverRoutesMenuKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Constants.*;
import static org.project.util.constants.Messages.ROUTES_MENU;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Component
public class DriverRoutes extends UpdateHandler {
    private final RouteService routeService;

    public DriverRoutes(RouteService routeService) {
        this.routeService = routeService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsAnyHandler(update, DRIVER_ROUTES, ROUTE_MENU_NEXT);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        if (!userPhase.getPhase().equals(handlerPhase)) {
            routeService.updateAllEditing(userId);
        }

        updateUserPhase(userPhase, handlerPhase);

        if (isUpdateContainsHandler(update, ROUTE_MENU_NEXT)) {
            int page = getOffsetParamFromUpdateByHandler(update, ROUTE_MENU_NEXT);
            PageRequest pageRequest = of(page, DEFAULT_ROUTE_LIMIT, ASC, DEFAULT_ID_FIELD);
            Page<Route> routes = routeService.getAllCreatedRoutes(pageRequest, userId);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendRemovableMessage(userId, ROUTES_MENU, getDriverRoutesMenuKeyboard(routes, ROUTE_MENU_NEXT, ROUTES_MAIN_MENU));

            return;
        }

        PageRequest pageRequest = of(DEFAULT_OFFSET, DEFAULT_ROUTE_LIMIT, ASC, DEFAULT_ID_FIELD);
        Page<Route> routes = routeService.getAllCreatedRoutes(pageRequest, userId);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        sendRemovableMessage(userId, ROUTES_MENU, getDriverRoutesMenuKeyboard(routes, ROUTE_MENU_NEXT, ROUTES_MAIN_MENU));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_ROUTES);
    }
}
