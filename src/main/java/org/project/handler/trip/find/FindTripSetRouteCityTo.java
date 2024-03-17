package org.project.handler.trip.find;

import org.project.handler.UpdateHandler;
import org.project.model.*;
import org.project.service.CityService;
import org.project.service.RouteService;
import org.project.service.TripService;
import org.project.util.UpdateHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.*;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Buttons.BACK_TO_CITIES;
import static org.project.util.constants.Buttons.BACK_TO_COUNTRIES;
import static org.project.util.constants.Constants.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;

@Component
public class FindTripSetRouteCityTo extends UpdateHandler {
	private final CityService cityService;
	private final RouteService routeService;
	private final TripService tripService;

	public FindTripSetRouteCityTo(CityService cityService, RouteService routeService, TripService tripService) {
		this.cityService = cityService;
		this.routeService = routeService;
		this.tripService = tripService;
	}

	@Override
	public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
		return isUpdateContainsHandler(update, FIND_TRIP_CITY_TO);
	}

	@Override
	public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
		long userId = getUserIdFromUpdate(update);
		updateUserPhase(userPhase, handlerPhase);
		Route route = routeService.getNewPassengerRoute(userId);

		if (isMessageSentInsteadOfButtonClick(update)) {
			return;
		}

		if (isUpdateContainsHandler(update, FIND_TRIP_CITY_TO_NEXT)) {
			int page = getOffsetParamFromUpdateByHandler(update, FIND_TRIP_CITY_TO_NEXT);
			PageRequest pageRequest = of(page, DEFAULT_CITY_LIMIT);

			deleteRemovableMessagesAndEraseAllFromRepo(userId);

			Page<City> cities = cityService.findAllCities(pageRequest, route.getCountryTo());

			sendRemovableMessage(userId, PROVIDE_CITY_TO,
					getAvailableCitiesKeyboard(cities, FIND_TRIP_CITY_TO_NEXT, FIND_TRIP_CITY_TO,
							FIND_TRIP_COUNTRY_TO_NEXT, BACK_TO_COUNTRIES));

			return;
		}

		route = routeService.updateRouteCityTo(route, UpdateHelper.getCallbackQueryIdParamFromUpdate(update));

		updateUserPhase(userPhase, FIND_TRIP_MENU);

		deleteRemovableMessagesAndEraseAllFromRepo(userId);

		sendMessage(userId, format(CITY_TO_PROVIDED, route.getCityTo().getName()));

		sendRemovableMessage(userId, format(FIND_TRIP_LOOKING_FOR_TRIPS, route.getSimplifiedRoute()));

		Page<Trip> trips = tripService.findAllCreatedNonDriverTrips(route, of(DEFAULT_OFFSET, DEFAULT_TRIP_LIMIT));

		if (trips.isEmpty()) {
			sendRemovableMessage(userId, FIND_TRIP_NO_TRIPS, getNoTripsKeyboard(route.getId(), FIND_TRIP_CITY_TO_NEXT));

			return;
		}

		sendRemovableMessage(userId, format(FIND_TRIP_CHOOSE_TRIPS, route.getFormattedData()),
				getAvailableTripsForPassengerKeyboard(trips, FIND_TRIP_MENU_NEXT, FIND_TRIP_MENU_DETAILS,
						FIND_TRIP_CITY_TO_NEXT, BACK_TO_CITIES));
	}

	@Override
	public void initHandler() {
		handlerPhase = getPhaseService().getPhaseByHandlerName(FIND_TRIP_CITY_TO);
	}

}
