package org.project.handler.route.edit;

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
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Constants.DEFAULT_CITY_LIMIT;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.EDIT_ROUTE_CITY_FROM;
import static org.project.util.enums.HandlerName.EDIT_ROUTE_CITY_FROM_NEXT;
import static org.project.util.enums.Status.CREATED;
import static org.project.util.enums.Status.EDITING;

@Component
public class EditRouteSetCityFrom extends EditRoute {
    private final CityService cityService;
    private final RouteService routeService;

    public EditRouteSetCityFrom(CityService cityService, RouteService routeService) {
        this.cityService = cityService;
        this.routeService = routeService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return super.isApplicable(phaseOptional, update) || isUpdateCallbackEqualsHandler(update, EDIT_ROUTE_CITY_FROM);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        Route route;

        if (!userPhase.getPhase().equals(handlerPhase)) {
            route = routeService.getRoute(getOffsetParamFromUpdateByHandler(update, handlerPhase.getHandlerName()));
            routeService.updateRouteStatus(route, EDITING);
        }

        if (update.hasMessage()) {
            sendRemovableMessage(userId, WRONG_ROUTE_DATA_PROVIDED);

            return;
        }

        if (isUpdateContainsHandler(update, EDIT_ROUTE_CITY_FROM_NEXT) || !userPhase.isPhaseEquals(handlerPhase)) {
            updateUserPhase(userPhase, handlerPhase);

            route = routeService.getEditingRoute(userId);

            int page = getOffsetParamFromUpdateByHandler(update, EDIT_ROUTE_CITY_FROM_NEXT);
            PageRequest pageRequest = PageRequest.of(page, DEFAULT_CITY_LIMIT);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendRemovableMessage(userId, PROVIDE_CITY_FROM, getAvailableCitiesKeyboard(
                    cityService.findAllUnusedCitiesFrom(route, pageRequest), EDIT_ROUTE_CITY_FROM_NEXT,
                    EDIT_ROUTE_CITY_FROM));

            return;
        }

        route = routeService.getEditingRoute(userId);

        route = routeService.updateRouteCityFrom(route, getCallbackQueryIdParamFromUpdate(update));

        routeService.updateRouteStatus(route, CREATED);

        sendMessage(userId, format(CITY_FROM_PROVIDED, route.getCityFrom().getName()));

        sendRouteDetailsAndUpdateUserPhase(userId, route, userPhase);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(EDIT_ROUTE_CITY_FROM);
    }
}
