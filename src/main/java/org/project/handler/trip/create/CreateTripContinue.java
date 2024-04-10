package org.project.handler.trip.create;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.RouteService;
import org.project.service.TripService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.getCurrenciesKeyboard;
import static org.project.util.Keyboards.getDriverRoutesKeyboard;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.UpdateHelper.isUpdateContainsHandler;
import static org.project.util.constants.Constants.*;
import static org.project.util.constants.Messages.DRIVER_TRIP_EDIT_OTHER_INFO;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class CreateTripContinue extends UpdateHandler {
    private final TripService tripService;
    private final RouteService routeService;

    public CreateTripContinue(TripService tripService, RouteService routeService) {
        this.tripService = tripService;
        this.routeService = routeService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandlerPhase(update) || isUpdateContainsHandler(update, START_TRIP_OVER_CREATION);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        updateUserPhase(userPhase, handlerPhase);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        Trip trip = tripService.getNewTrip(userId);

        if(isUpdateContainsHandler(update, START_TRIP_OVER_CREATION)){
            tripService.deleteTrip(trip.getId());

            PageRequest pageRequest = of(DEFAULT_OFFSET, DEFAULT_ROUTE_LIMIT, ASC, DEFAULT_ID_FIELD);

            sendEditableMessage(userId, TRIP_CREATING_CHOOSE_ROUTE, getDriverRoutesKeyboard(
                    routeService.getAllCreatedRoutes(pageRequest, userId), CREATE_TRIP_CHOOSE_ROUTE_NEXT, CREATE_TRIP_CHOOSE_ROUTE));

            return;
        }

        if (isEmpty(trip.getRoute())) {
            PageRequest pageRequest = of(DEFAULT_OFFSET, DEFAULT_ROUTE_LIMIT, ASC, DEFAULT_ID_FIELD);

            sendEditableMessage(userId, TRIP_CREATING_CHOOSE_ROUTE, getDriverRoutesKeyboard(
                    routeService.getAllCreatedRoutes(pageRequest, userId), CREATE_TRIP_CHOOSE_ROUTE_NEXT,
                    CREATE_TRIP_CHOOSE_ROUTE));
            return;
        }

        if (isEmpty(trip.getDepartureDate())) {
            sendEditableMessage(userId, DRIVER_TRIP_ENTER_DEPARTURE_DATE);

            updateUserPhase(userPhase, CREATE_TRIP_PROVIDE_DEPARTURE_DATE);

            return;
        }

        if (isEmpty(trip.getDepartureTime())) {
            sendEditableMessage(userId, DRIVER_TRIP_ENTER_DEPARTURE_TIME);

            updateUserPhase(userPhase, CREATE_TRIP_PROVIDE_DEPARTURE_TIME);

            return;
        }

        if (isEmpty(trip.getArrivalDate())) {
            sendEditableMessage(userId, DRIVER_TRIP_ENTER_ARRIVAL_DATE);

            updateUserPhase(userPhase, CREATE_TRIP_PROVIDE_ARRIVAL_DATE);

            return;
        }

        if (isEmpty(trip.getArrivalTime())) {
            sendEditableMessage(userId, DRIVER_TRIP_ENTER_ARRIVAL_TIME);

            updateUserPhase(userPhase, CREATE_TRIP_PROVIDE_ARRIVAL_TIME);

            return;
        }

        if (!StringUtils.hasText(trip.getPickupPoint())) {
            sendEditableMessage(userId, format(DRIVER_TRIP_ENTER_PICKUP_POINT, trip.getRoute().getCityFrom().getName()));

            updateUserPhase(userPhase, CREATE_TRIP_PROVIDE_PICKUP_POINT);

            return;
        }

        if (!StringUtils.hasText(trip.getDropOffPoint())) {
            sendEditableMessage(userId, format(DRIVER_TRIP_ENTER_DROPOFF_POINT, trip.getRoute().getCityTo().getName()));

            updateUserPhase(userPhase, CREATE_TRIP_PROVIDE_DROP_OFF_POINT);

            return;
        }

        if (isEmpty(trip.getCurrency())) {
            sendEditableMessage(userId, DRIVER_TRIP_CHOOSE_CURRENCY, getCurrenciesKeyboard());

            updateUserPhase(userPhase, CREATE_TRIP_CHOOSE_CURRENCY);

            return;
        }

        if (trip.getPrice() == 0) {
            sendEditableMessage(userId, format(DRIVER_TRIP_ENTER_PRICE, trip.getCurrency().name()));

            updateUserPhase(userPhase, CREATE_TRIP_PROVIDE_PRICE);

            return;
        }

        if (!StringUtils.hasText(trip.getBaggageInfo())) {
            sendEditableMessage(userId, DRIVER_TRIP_ENTER_BAGGAGE_INFO);

            updateUserPhase(userPhase, CREATE_TRIP_PROVIDE_BAGGAGE_INFO);

            return;
        }

        if (!StringUtils.hasText(trip.getOtherInfo())) {
            sendEditableMessage(userId, DRIVER_TRIP_EDIT_OTHER_INFO);

            updateUserPhase(userPhase, CREATE_TRIP_PROVIDE_OTHER_INFO);
        }
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(CONTINUE_TRIP_CREATION);
    }
}
