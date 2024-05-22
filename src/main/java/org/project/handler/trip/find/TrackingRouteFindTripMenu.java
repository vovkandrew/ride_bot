package org.project.handler.trip.find;

import org.project.handler.UpdateHandler;
import org.project.model.*;
import org.project.service.RouteService;
import org.project.service.TripService;
import org.project.util.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.*;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Buttons.BACK_BUTTON;
import static org.project.util.constants.Buttons.BACK_TO_CITIES;
import static org.project.util.constants.Constants.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;


@Component
public class TrackingRouteFindTripMenu extends UpdateHandler {
	private final RouteService routeService;
	private final TripService tripService;
	public TrackingRouteFindTripMenu(RouteService routeService, TripService tripService) {
		this.routeService = routeService;
		this.tripService = tripService;
	}

	@Override
	public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
		return isUpdateContainsHandlerPhase(update);
	}

	@Override
	public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
		long userId = getUserIdFromUpdate(update);
		updateUserPhase(userPhase, handlerPhase);
		Route route;

		if(isUpdateCallbackEqualsHandler(update, TRACK_ROUTE_FIND_TRIP_FIRST)){
			routeService.unpickAllRoutes(userId);

			route = routeService.getRoute(getCallbackQueryIdParamFromUpdate(update));

			route.setStatus(Status.PICKED);
		}else {
			route = routeService.getPickedPassengerRoute(userId);
		}

		if (isMessageSentInsteadOfButtonClick(update)) {
			return;
		}

		if (isUpdateContainsHandler(update, TRACK_ROUTE_FIND_TRIP_NEXT)) {
			int page = getOffsetParamFromUpdateByHandler(update, TRACK_ROUTE_FIND_TRIP_NEXT);
			PageRequest pageRequest = of(page, DEFAULT_TRIP_LIMIT);

			deleteRemovableMessagesAndEraseAllFromRepo(userId);

			Page<Trip> trips = tripService.findAllCreatedNonDriverTrips(route, pageRequest);

			sendRemovableMessage(userId, format(FIND_TRIP_CHOOSE_TRIPS, route.getFormattedData()),
					getAvailableTripsForPassengerKeyboard(trips, TRACK_ROUTE_FIND_TRIP_NEXT, TRACK_ROUTE_TRIP_DETAILS,
							TRACKING_ROUTES, BACK_BUTTON));

			return;
		}

		deleteRemovableMessagesAndEraseAllFromRepo(userId);

		Page<Trip> trips = tripService.findAllCreatedNonDriverTrips(route, of(DEFAULT_OFFSET, DEFAULT_TRIP_LIMIT));

		if (trips.isEmpty()) {
			sendRemovableMessage(userId, FIND_TRIP_NO_TRIPS, getNoTrackTripsKeyboard(TRACKING_ROUTES));

			return;
		}

		sendRemovableMessage(userId, format(FIND_TRIP_CHOOSE_TRIPS, route.getFormattedData()),
				getAvailableTripsForPassengerKeyboard(trips, TRACK_ROUTE_FIND_TRIP_NEXT, TRACK_ROUTE_TRIP_DETAILS,
						TRACKING_ROUTES, BACK_TO_ROUTES));
	}

	@Override
	public void initHandler() {
		handlerPhase = getPhaseService().getPhaseByHandlerName(TRACK_ROUTE_FIND_TRIP);
	}
}