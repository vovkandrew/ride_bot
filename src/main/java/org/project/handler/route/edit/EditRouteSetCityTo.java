package org.project.handler.route.edit;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.Route;
import org.project.model.UserPhase;
import org.project.service.CityService;
import org.project.service.RouteService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.getAvailableCitiesKeyboard;
import static org.project.util.Keyboards.getDriverRouteMenuKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Constants.DEFAULT_CITY_LIMIT;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.*;
import static org.project.util.enums.Status.CREATED;
import static org.project.util.enums.Status.EDITING;
import static org.springframework.data.domain.PageRequest.of;

@Component
public class EditRouteSetCityTo extends UpdateHandler {
    private final CityService cityService;
    private final RouteService routeService;

    public EditRouteSetCityTo(CityService cityService, RouteService routeService) {
        this.cityService = cityService;
        this.routeService = routeService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return super.isApplicable(phaseOptional, update) || isUpdateCallbackEqualsHandler(update, EDIT_ROUTE_CITY_TO);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        Route route;

        if (!userPhase.getPhase().equals(handlerPhase)) {
            route = routeService.getRoute(getOffsetParamFromUpdateByHandler(update, handlerPhase.getHandlerName()));
            routeService.updateRouteStatus(route, EDITING);
        }

        if (isMessageSentInsteadOfButtonClick(update)) {
            return;
        }

        if (isUpdateContainsHandler(update, EDIT_ROUTE_CITY_TO_NEXT) || !userPhase.isPhaseEquals(handlerPhase)) {
            updateUserPhase(userPhase, handlerPhase);

            route = routeService.getEditingRoute(userId);

            int page = getOffsetParamFromUpdateByHandler(update, EDIT_ROUTE_CITY_TO_NEXT);
            PageRequest pageRequest = of(page, DEFAULT_CITY_LIMIT);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendRemovableMessage(userId, PROVIDE_CITY_TO, getAvailableCitiesKeyboard(
                    cityService.findAllUnusedCitiesTo(route, pageRequest), EDIT_ROUTE_CITY_TO_NEXT, EDIT_ROUTE_CITY_TO));

            return;
        }

        route = routeService.getEditingRoute(userId);
        route = routeService.updateRouteCityTo(route, getCallbackQueryIdParamFromUpdate(update));

        updateUserPhase(userPhase, ROUTES_MAIN_MENU);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        sendMessage(userId, format(CITY_TO_PROVIDED, route.getCityTo().getName()));

        sendRemovableMessage(userId, joinMessages(format(ROUTE_DATA, route.getFormattedData()), ROUTE_DATA_INFO),
                getDriverRouteMenuKeyboard(route.getId()));

        routeService.updateRouteStatus(route, CREATED);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(EDIT_ROUTE_CITY_TO);
    }
}
