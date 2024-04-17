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
public class CreateRouteSetCountryTo extends UpdateHandler {
	private final CountryService countryService;
	private final CityService cityService;
	private final RouteService routeService;

	public CreateRouteSetCountryTo(CountryService countryService, CityService cityService, RouteService routeService) {
		this.countryService = countryService;
		this.cityService = cityService;
		this.routeService = routeService;
	}

	@Override
	public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
		return isUpdateContainsHandler(update, SET_ROUTE_COUNTRY_TO);
	}

	@Override
	public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
		long telegramUserId = getTelegramUserIdFromUpdate(update);
		updateUserPhase(userPhase, handlerPhase);

		if (isMessageSentInsteadOfButtonClick(update)) {
			return;
		}

		Route route = routeService.getNewDriverRoute(telegramUserId);

		if (isUpdateContainsHandler(update, SET_ROUTE_COUNTRY_TO_NEXT)) {
			int page = getOffsetParamFromUpdateByHandler(update, SET_ROUTE_COUNTRY_TO_NEXT);
			PageRequest pageRequest = of(page, DEFAULT_COUNTRY_LIMIT, ASC, DEFAULT_NAME_FIELD);

			deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

			sendRemovableMessage(telegramUserId, PROVIDE_COUNTY_TO,
					getAvailableCountriesKeyboard(countryService.findAllCountriesExcept(pageRequest, route.getCountryFrom()),
							SET_ROUTE_COUNTRY_TO_NEXT, SET_ROUTE_COUNTRY_TO, Optional.of(SET_ROUTE_CITY_FROM_NEXT),
							Optional.of(BACK_TO_CITIES)));

			return;
		}

		route = routeService.updateRouteCountryTo(route, getCallbackQueryIdParamFromUpdate(update));
		PageRequest pageRequest = of(DEFAULT_OFFSET, DEFAULT_COUNTRY_LIMIT);

		updateUserPhase(userPhase, SET_ROUTE_CITY_TO);

		deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

		sendMessage(telegramUserId, format(COUNTRY_TO_PROVIDED, route.getCountryTo().getName()));

		sendRemovableMessage(telegramUserId, PROVIDE_CITY_TO,
				getAvailableCitiesKeyboard(cityService.findAllUnusedCitiesTo(route, pageRequest),
						SET_ROUTE_CITY_TO_NEXT, SET_ROUTE_CITY_TO, Optional.of(SET_ROUTE_COUNTRY_TO_NEXT),
						Optional.of(BACK_TO_COUNTRIES)));
	}

	@Override
	public void initHandler() {
		handlerPhase = getPhaseService().getPhaseByHandlerName(SET_ROUTE_COUNTRY_TO);
	}

}
