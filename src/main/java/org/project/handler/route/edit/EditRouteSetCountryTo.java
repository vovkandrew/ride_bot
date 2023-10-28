package org.project.handler.route.edit;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.Route;
import org.project.model.UserPhase;
import org.project.service.CityService;
import org.project.service.CountryService;
import org.project.service.RouteService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static org.project.util.Keyboards.getAvailableCitiesKeyboard;
import static org.project.util.Keyboards.getAvailableCountriesKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Constants.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.*;
import static org.project.util.enums.Status.EDITING;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Component
public class EditRouteSetCountryTo extends UpdateHandler {
    private final CountryService countryService;
    private final RouteService routeService;
    private final CityService cityService;

    public EditRouteSetCountryTo(CountryService countryService, RouteService routeService, CityService cityService) {
        this.countryService = countryService;
        this.routeService = routeService;
        this.cityService = cityService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return super.isApplicable(phaseOptional, update) || isUpdateContainsHandler(update, EDIT_ROUTE_COUNTRY_TO);
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

        if (isUpdateContainsHandler(update, EDIT_ROUTE_COUNTRY_TO_NEXT) || !userPhase.isPhaseEquals(handlerPhase)) {
            updateUserPhase(userPhase, handlerPhase);

            route = routeService.getEditingRoute(userId);

            int page = getOffsetParamFromUpdateByHandler(update, EDIT_ROUTE_COUNTRY_TO_NEXT);
            PageRequest pageRequest = of(page, DEFAULT_COUNTRY_LIMIT, ASC, DEFAULT_NAME_FIELD);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendRemovableMessage(userId, PROVIDE_COUNTY_TO, getAvailableCountriesKeyboard(
                    countryService.findAllCountriesExcept(pageRequest, route.getCountryFrom(), route.getCountryTo()),
                    EDIT_ROUTE_COUNTRY_TO_NEXT, EDIT_ROUTE_COUNTRY_TO));

            return;
        }

        route = routeService.getEditingRoute(userId);
        route = routeService.updateRouteCountryTo(route, getCallbackQueryIdParamFromUpdate(update));

        updateUserPhase(userPhase, EDIT_ROUTE_CITY_TO);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        sendMessage(userId, String.format(COUNTRY_TO_PROVIDED, route.getCountryTo().getName()));

        int page = getOffsetParamFromUpdateByHandler(update, EDIT_ROUTE_CITY_TO_NEXT);
        PageRequest pageRequest = of(page, DEFAULT_CITY_LIMIT);

        sendRemovableMessage(userId, PROVIDE_CITY_TO, getAvailableCitiesKeyboard(
                cityService.findAllUnusedCitiesTo(route, pageRequest), EDIT_ROUTE_CITY_TO_NEXT,
                EDIT_ROUTE_CITY_TO));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(EDIT_ROUTE_COUNTRY_TO);
    }
}
