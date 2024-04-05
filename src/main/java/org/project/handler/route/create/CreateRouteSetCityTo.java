package org.project.handler.route.create;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.Route;
import org.project.model.UserPhase;
import org.project.service.CityService;
import org.project.service.RouteService;
import org.project.util.constants.Constants;
import org.project.util.enums.Status;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.getAvailableCitiesKeyboard;
import static org.project.util.Keyboards.getDriverRouteMenuKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Buttons.BACK_TO_COUNTRIES;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;

@Component
public class CreateRouteSetCityTo extends UpdateHandler {
    private final CityService cityService;
    private final RouteService routeService;

    public CreateRouteSetCityTo(CityService cityService, RouteService routeService) {
        this.cityService = cityService;
        this.routeService = routeService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandler(update, SET_ROUTE_CITY_TO);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        updateUserPhase(userPhase, handlerPhase);
        Route route = routeService.getNewDriverRoute(userId);

        if (isMessageSentInsteadOfButtonClick(update)) {
            return;
        }

        if (isUpdateContainsHandler(update, SET_ROUTE_CITY_TO_NEXT)) {
            int page = getOffsetParamFromUpdateByHandler(update, SET_ROUTE_CITY_TO_NEXT);
            PageRequest pageRequest = of(page, Constants.DEFAULT_CITY_LIMIT);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendRemovableMessage(userId, PROVIDE_CITY_TO,
                    getAvailableCitiesKeyboard(cityService.findAllUnusedCitiesTo(route, pageRequest),
                            SET_ROUTE_CITY_TO_NEXT, SET_ROUTE_CITY_TO, Optional.of(SET_ROUTE_COUNTRY_TO_NEXT),
                            Optional.of(BACK_TO_COUNTRIES)));

            return;
        }

        route = routeService.updateRouteCityTo(route, getCallbackQueryIdParamFromUpdate(update));

        Optional<Route> similarRoute = routeService.findDriverDeletedRoute(userId, route);

        if (similarRoute.isPresent()){
            routeService.deleteRoute(route);

			route = similarRoute.get();

            route.setStatus(Status.CREATED);

            routeService.saveRoute(route);
        } else {
            routeService.createRoute(route);
        }

        updateUserPhase(userPhase, ROUTES_MAIN_MENU);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        sendMessage(userId, format(CITY_TO_PROVIDED, route.getCityTo().getName()));

        sendMessage(userId, format(NEW_ROUTE_CREATED, route.getCityFrom().getName(), route.getCityTo().getName()));

        sendRemovableMessage(userId, joinMessages(format(ROUTE_DATA, route.getFormattedData()), ROUTE_DATA_INFO),
                getDriverRouteMenuKeyboard(route.getId()));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(SET_ROUTE_CITY_TO);
    }
}
