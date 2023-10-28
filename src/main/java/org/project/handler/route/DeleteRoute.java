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

import static java.lang.String.format;
import static org.project.util.Keyboards.getDriverRoutesMenuKeyboard;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.UpdateHelper.isUpdateContainsHandler;
import static org.project.util.constants.Constants.*;
import static org.project.util.constants.Messages.NEW_ROUTE_CREATED;
import static org.project.util.constants.Messages.ROUTES_MENU;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Component
public class DeleteRoute extends UpdateHandler {
    private final RouteService routeService;

    public DeleteRoute(RouteService routeService) {
        this.routeService = routeService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return super.isApplicable(phaseOptional, update) || isUpdateContainsHandler(update, ROUTE_DELETION);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        updateUserPhase(userPhase, handlerPhase);

        Route route = routeService.getNewRoute(userId);
        routeService.deleteRoute(route);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        sendMessage(userId, format(NEW_ROUTE_CREATED, route.getFormattedData()));

        PageRequest pageRequest = of(DEFAULT_OFFSET, DEFAULT_ROUTE_LIMIT, ASC, DEFAULT_ID_FIELD);
        Page<Route> routes = routeService.getAllCreatedRoutes(pageRequest, userId);

        sendRemovableMessage(userId, format(ROUTES_MENU, routes.getSize()), getDriverRoutesMenuKeyboard(
                routes, ROUTE_MENU_NEXT, ROUTES_MAIN_MENU));

        updateUserPhase(userPhase, ROUTES_MAIN_MENU);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(ROUTE_DELETION);
    }
}
