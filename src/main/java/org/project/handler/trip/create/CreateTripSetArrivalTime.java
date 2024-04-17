package org.project.handler.trip.create;

import org.project.handler.UpdateHandler;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.TripService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.project.util.UpdateHelper.getTelegramUserIdFromUpdate;
import static org.project.util.UpdateHelper.getUserInputFromUpdate;
import static org.project.util.constants.Constants.TIME_FORMAT;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.TIME_PATTERN;
import static org.project.util.enums.HandlerName.CREATE_TRIP_PROVIDE_ARRIVAL_TIME;
import static org.project.util.enums.HandlerName.CREATE_TRIP_PROVIDE_PICKUP_POINT;

@Component
public class CreateTripSetArrivalTime extends UpdateHandler {
    private final TripService tripService;

    public CreateTripSetArrivalTime(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);

        Trip trip = tripService.getNewTrip(telegramUserId);

        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, TIME_PATTERN) && trip.verifyArrivalTime(userInput)) {
            trip = tripService.updateTripArrivalTime(trip, userInput);

            editMessage(telegramUserId, format(DRIVER_TRIP_ARRIVAL_TIME_PROVIDED, trip.getArrivalTime().format(ofPattern(TIME_FORMAT))));

            deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

            sendEditableMessage(telegramUserId, format(DRIVER_TRIP_ENTER_PICKUP_POINT, trip.getRoute().getCityFrom().getName()));

            updateUserPhase(userPhase, CREATE_TRIP_PROVIDE_PICKUP_POINT);

            return;
        }

        sendRemovableMessage(telegramUserId, DRIVER_TRIP_WRONG_ARRIVAL_TIME );
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(CREATE_TRIP_PROVIDE_ARRIVAL_TIME);
    }
}
