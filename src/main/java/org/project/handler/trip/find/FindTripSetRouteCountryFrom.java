package org.project.handler.trip.find;

import org.project.handler.UpdateHandler;
import org.project.model.Country;
import org.project.model.Phase;
import org.project.model.Route;
import org.project.model.UserPhase;
import org.project.service.CityService;
import org.project.service.CountryService;
import org.project.service.RouteService;
import org.springframework.data.domain.Page;
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
import static org.project.util.enums.Status.NEW;
import static org.project.util.enums.UserType.PASSENGER;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Component
public class FindTripSetRouteCountryFrom extends UpdateHandler {
	private final CountryService countryService;
	private final CityService cityService;
	private final RouteService routeService;

	public FindTripSetRouteCountryFrom(CountryService countryService, CityService cityService,
	                                   RouteService routeService) {
		this.countryService = countryService;
		this.cityService = cityService;
		this.routeService = routeService;
	}

	@Override
	public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
		return super.isApplicable(phaseOptional, update)
				|| isUpdateContainsAnyHandler(update, FINDING_TRIP, FIND_TRIP_COUNTRY_FROM);
	}

	@Override
	public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
		long userId = getUserIdFromUpdate(update);
		updateUserPhase(userPhase, handlerPhase);

		routeService.deleteAllNewPassengersRoutes(userId);

		if (isMessageSentInsteadOfButtonClick(update)) {
			return;
		}

		if (isUpdateCallbackEqualsHandler(update, FINDING_TRIP)) {
			sendMessage(userId, FIND_TRIP);
		}

		PageRequest pageRequest;

		if (isUpdateContainsAnyHandler(update, FINDING_TRIP, FIND_TRIP_COUNTRY_FROM_NEXT,
				FIND_TRIP_COUNTRY_FROM_BACK)) {
			int page = getOffsetParamFromUpdateByHandler(update, FIND_TRIP_COUNTRY_FROM_NEXT);
			pageRequest = of(page, DEFAULT_COUNTRY_LIMIT, ASC, DEFAULT_NAME_FIELD);

			deleteRemovableMessagesAndEraseAllFromRepo(userId);

			Page<Country> countries = countryService.findAllCountries(pageRequest);

			sendRemovableMessage(userId, PROVIDE_COUNTY_FROM,
					getAvailableCountriesKeyboard(countries, FIND_TRIP_COUNTRY_FROM_NEXT,
							FIND_TRIP_COUNTRY_FROM, PASSENGER_MENU));

			return;
		}

		Route route = Route.builder().telegramUserId(userId).status(NEW).userType(PASSENGER).build();
		route = routeService.updateRouteCountryFrom(route, getCallbackQueryIdParamFromUpdate(update));
		pageRequest = of(DEFAULT_OFFSET, DEFAULT_COUNTRY_LIMIT, ASC, DEFAULT_NAME_FIELD);

		updateUserPhase(userPhase, FIND_TRIP_CITY_FROM);

		deleteRemovableMessagesAndEraseAllFromRepo(userId);

		sendMessage(userId, format(COUNTRY_FROM_PROVIDED, route.getCountryFrom().getName()));

		sendRemovableMessage(userId, PROVIDE_CITY_FROM, getAvailableCitiesKeyboard(
				cityService.findAllCities(pageRequest, route.getCountryFrom()), FIND_TRIP_CITY_FROM_NEXT,
				FIND_TRIP_CITY_FROM, FIND_TRIP_COUNTRY_FROM_BACK));
	}

	@Override
	public void initHandler() {
		handlerPhase = getPhaseService().getPhaseByHandlerName(FIND_TRIP_COUNTRY_FROM);
	}

}
