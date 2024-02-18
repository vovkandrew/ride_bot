package org.project.handler.trip.find;

import org.project.handler.UpdateHandler;
import org.project.model.*;
import org.project.service.RouteService;
import org.project.service.TripService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.getAvailableCitiesKeyboard;
import static org.project.util.Keyboards.getAvailableTripsForPassengerKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Constants.*;
import static org.project.util.constants.Messages.FIND_TRIP_CHOOSE_TRIPS;
import static org.project.util.constants.Messages.PROVIDE_CITY_TO;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;


@Component
public class FindTripMenu extends UpdateHandler {
    private final RouteService routeService;
    private final TripService tripService;
    public FindTripMenu(RouteService routeService, TripService tripService) {
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
        Route route = routeService.getNewPassengerRoute(userId);

        if (isMessageSentInsteadOfButtonClick(update)) {
            return;
        }

        if (isUpdateContainsHandler(update, FIND_TRIP_MENU_NEXT)) {
            int page = getOffsetParamFromUpdateByHandler(update, FIND_TRIP_MENU_NEXT);
            PageRequest pageRequest = of(page, DEFAULT_TRIP_LIMIT);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            Page<Trip> trips = tripService.findAllCreatedNonDriverTrips(route, pageRequest);

            sendRemovableMessage(userId, format(FIND_TRIP_CHOOSE_TRIPS, route.getFormattedData()),
                    getAvailableTripsForPassengerKeyboard(trips, FIND_TRIP_MENU_NEXT, FIND_TRIP_MENU_DETAILS));

            return;
        }

        deleteRemovableMessagesAndEraseAllFromRepo(userId);
        Page<Trip> trips = tripService.findAllCreatedNonDriverTrips(route, of(DEFAULT_OFFSET, DEFAULT_TRIP_LIMIT));
        sendRemovableMessage(userId, format(FIND_TRIP_CHOOSE_TRIPS, route.getFormattedData()),
                getAvailableTripsForPassengerKeyboard(trips, FIND_TRIP_MENU_NEXT, FIND_TRIP_MENU_DETAILS));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(FIND_TRIP_MENU);
    }
}