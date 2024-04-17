package org.project.handler.route;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.Route;
import org.project.model.UserPhase;
import org.project.service.RouteService;
import org.project.service.TripService;
import org.project.util.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.getDriverRouteMenuKeyboard;
import static org.project.util.Keyboards.getDriverRoutesMenuKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Constants.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Component
public class DeleteRoute extends UpdateHandler {
    private final RouteService routeService;
    private final TripService tripService;

    public DeleteRoute(RouteService routeService, TripService tripService) {
        this.routeService = routeService;
	    this.tripService = tripService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return super.isApplicable(phaseOptional, update) || isUpdateContainsHandler(update, ROUTE_DELETION);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);
        updateUserPhase(userPhase, handlerPhase);

        long routeId = getCallbackQueryIdParamFromUpdate(update);

        Route route = routeService.getRoute(routeId);

        if(tripService.isNonExpiredTripsExists(routeId, LocalDate.now())){
            deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

            sendRemovableMessage(telegramUserId, format(RESTRICTED_ROUTE_DELETION, route.getSimplifiedRoute()));

            sendRemovableMessage(telegramUserId, joinMessages(format(ROUTE_DATA, route.getFormattedData()), ROUTE_DATA_INFO),
                    getDriverRouteMenuKeyboard(route.getId()));

            updateUserPhase(userPhase, ROUTES_MAIN_MENU);

            return;
        }
        route.setStatus(Status.DELETED);

        deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

        sendMessage(telegramUserId, format(ROUTE_DELETED, route.getSimplifiedRoute()));

        PageRequest pageRequest = of(DEFAULT_OFFSET, DEFAULT_ROUTE_LIMIT, ASC, DEFAULT_ID_FIELD);
        Page<Route> routes = routeService.getAllCreatedDriverRoutes(pageRequest, telegramUserId);

        sendRemovableMessage(telegramUserId, format(ROUTES_MENU, routes.getSize()), getDriverRoutesMenuKeyboard(
                routes, ROUTE_MENU_NEXT, ROUTES_MAIN_MENU));

        updateUserPhase(userPhase, ROUTES_MAIN_MENU);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(ROUTE_DELETION);
    }
}
