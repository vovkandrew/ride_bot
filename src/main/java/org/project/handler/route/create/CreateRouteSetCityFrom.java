package org.project.handler.route.create;

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
import static org.project.util.constants.Buttons.BACK_TO_CITIES;
import static org.project.util.constants.Buttons.BACK_TO_COUNTRIES;
import static org.project.util.constants.Constants.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Component
public class CreateRouteSetCityFrom extends UpdateHandler {
	private final CountryService countryService;
	private final CityService cityService;
	private final RouteService routeService;

	public CreateRouteSetCityFrom(CountryService countryService, CityService cityService, RouteService routeService) {
		this.countryService = countryService;
		this.cityService = cityService;
		this.routeService = routeService;
	}

	@Override
	public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
		return isUpdateContainsHandler(update, SET_ROUTE_CITY_FROM);
	}

	@Override
	public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
		long userId = getUserIdFromUpdate(update);
		updateUserPhase(userPhase, handlerPhase);

		if (isMessageSentInsteadOfButtonClick(update)) {
			return;
		}

		Route route = routeService.getNewDriverRoute(userId);

		if (isUpdateContainsHandler(update, SET_ROUTE_CITY_FROM_NEXT)) {
			int page = getOffsetParamFromUpdateByHandler(update, SET_ROUTE_CITY_FROM_NEXT);

			PageRequest pageRequest = of(page, DEFAULT_CITY_LIMIT, ASC, DEFAULT_NAME_FIELD);

			deleteRemovableMessagesAndEraseAllFromRepo(userId);

			sendRemovableMessage(userId, PROVIDE_CITY_FROM,
					getAvailableCitiesKeyboard(cityService.findAllCities(pageRequest, route.getCountryFrom()),
							SET_ROUTE_CITY_FROM_NEXT, SET_ROUTE_CITY_FROM, Optional.of(SET_ROUTE_COUNTRY_FROM_NEXT),
							Optional.of(BACK_TO_COUNTRIES)));

			return;
		}

		route = routeService.updateRouteCityFrom(route, getCallbackQueryIdParamFromUpdate(update));
		updateUserPhase(userPhase, SET_ROUTE_COUNTRY_TO);

		PageRequest pageRequest = of(DEFAULT_OFFSET, DEFAULT_CITY_LIMIT, ASC, DEFAULT_NAME_FIELD);

		deleteRemovableMessagesAndEraseAllFromRepo(userId);

		sendMessage(userId, format(CITY_FROM_PROVIDED, route.getCityFrom().getName()));

		sendRemovableMessage(userId, PROVIDE_COUNTY_TO,
				getAvailableCountriesKeyboard(countryService.findAllCountriesExcept(pageRequest, route.getCountryFrom()),
						SET_ROUTE_COUNTRY_TO_NEXT, SET_ROUTE_COUNTRY_TO, Optional.of(SET_ROUTE_CITY_FROM_BACK),
						Optional.of(BACK_TO_CITIES)));
	}

	@Override
	public void initHandler() {
		handlerPhase = getPhaseService().getPhaseByHandlerName(SET_ROUTE_CITY_FROM);
	}

}
