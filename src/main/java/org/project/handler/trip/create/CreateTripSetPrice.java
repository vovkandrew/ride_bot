package org.project.handler.trip.create;

import org.project.handler.UpdateHandler;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.TripService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.String.format;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.UpdateHelper.getUserInputFromUpdate;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.PRICE;
import static org.project.util.enums.HandlerName.CREATE_TRIP_PROVIDE_BAGGAGE_INFO;
import static org.project.util.enums.HandlerName.CREATE_TRIP_PROVIDE_PRICE;

@Component
public class CreateTripSetPrice extends UpdateHandler {
    private final TripService tripService;

    public CreateTripSetPrice(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        Trip trip = tripService.getNewTrip(userId);

        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, PRICE)) {
            trip = tripService.updateTripPrice(trip, userInput);

            editMessage(userId, format(DRIVER_TRIP_PRICE_PROVIDED, trip.getPrice()));

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendEditableMessage(userId, DRIVER_TRIP_ENTER_BAGGAGE_INFO);

            updateUserPhase(userPhase, CREATE_TRIP_PROVIDE_BAGGAGE_INFO);

            return;
        }

        sendRemovableMessage(userId, DRIVER_TRIP_WRONG_PRICE);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(CREATE_TRIP_PROVIDE_PRICE);
    }
}
