package org.project.handler.route;

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
import static org.project.util.Keyboards.getDriverRouteMenuKeyboard;
import static org.project.util.UpdateHelper.getCallbackQueryIdParamFromUpdate;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.ROUTES_MAIN_MENU;

@Component
public class DriverRouteMenu extends UpdateHandler {
    private final RouteService routeService;

    public DriverRouteMenu(RouteService routeService) {
        this.routeService = routeService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandlerPhase(update);

    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        updateUserPhase(userPhase, ROUTES_MAIN_MENU);

        int dataParam = getCallbackQueryIdParamFromUpdate(update);

        Route route = routeService.getRoute(dataParam);

        sendRemovableMessage(userId, joinMessages(format(ROUTE_DATA, route.getFormattedData()), ROUTE_DATA_INFO),
                getDriverRouteMenuKeyboard(route.getId()));

    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(ROUTES_MAIN_MENU);
    }
}
