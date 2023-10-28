package org.project.handler.trip.find;

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

import static java.lang.String.format;
import static org.project.util.Keyboards.getAvailableCitiesKeyboard;
import static org.project.util.Keyboards.getAvailableCountriesKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Constants.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Component
public class FindTripSetRouteCountryTo extends UpdateHandler {
    private final CountryService countryService;
    private final CityService cityService;
    private final RouteService routeService;

    public FindTripSetRouteCountryTo(CountryService countryService, CityService cityService, RouteService routeService) {
        this.countryService = countryService;
        this.cityService = cityService;
        this.routeService = routeService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandlerPhase(update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        updateUserPhase(userPhase, handlerPhase);

        if (isMessageSentInsteadOfButtonClick(update)) {
            return;
        }

        Route route = routeService.getNewPassengerRoute(userId);

        if (isUpdateContainsHandler(update, FIND_TRIP_COUNTRY_TO_NEXT)) {
            int page = getOffsetParamFromUpdateByHandler(update, FIND_TRIP_COUNTRY_TO_NEXT);
            PageRequest pageRequest = of(page, DEFAULT_COUNTRY_LIMIT, ASC, DEFAULT_NAME_FIELD);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendRemovableMessage(userId, PROVIDE_COUNTY_TO, getAvailableCountriesKeyboard(
                    countryService.findAllCountriesExcept(pageRequest, route.getCountryFrom()),
                    FIND_TRIP_COUNTRY_TO_NEXT, FIND_TRIP_COUNTRY_TO));

            return;
        }

        route = routeService.updateRouteCountryTo(route, getCallbackQueryIdParamFromUpdate(update));

        PageRequest pageRequest = of(DEFAULT_OFFSET, DEFAULT_COUNTRY_LIMIT);

        updateUserPhase(userPhase, FIND_TRIP_CITY_TO);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        sendMessage(userId, format(COUNTRY_TO_PROVIDED, route.getCountryTo().getName()));

        sendRemovableMessage(userId, PROVIDE_CITY_TO, getAvailableCitiesKeyboard(
                cityService.findAllCities(pageRequest, route.getCountryTo()), FIND_TRIP_CITY_TO_NEXT, FIND_TRIP_CITY_TO));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(FIND_TRIP_COUNTRY_TO);
    }
}
