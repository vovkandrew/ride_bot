package org.project.handler.trip.create;

import org.project.handler.UpdateHandler;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.TripService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.String.format;
import static org.project.util.UpdateHelper.getTelegramUserIdFromUpdate;
import static org.project.util.UpdateHelper.getUserInputFromUpdate;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.GENERAL_MESSAGE_PATTERN;
import static org.project.util.enums.HandlerName.CREATE_TRIP_PROVIDE_BAGGAGE_INFO;
import static org.project.util.enums.HandlerName.CREATE_TRIP_PROVIDE_OTHER_INFO;

@Component
public class CreateTripSetBaggageInfo extends UpdateHandler {
    private final TripService tripService;

    public CreateTripSetBaggageInfo(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);

        Trip trip = tripService.getNewTrip(telegramUserId);

        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, GENERAL_MESSAGE_PATTERN)) {
            trip = tripService.updateTripBaggageInfo(trip, userInput);

            editMessage(telegramUserId, format(DRIVER_TRIP_BAGGAGE_INFO_PROVIDED, trip.getBaggageInfo()));

            deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

            sendEditableMessage(telegramUserId, DRIVER_TRIP_EDIT_OTHER_INFO);

            updateUserPhase(userPhase, CREATE_TRIP_PROVIDE_OTHER_INFO);

            return;
        }

        sendRemovableMessage(telegramUserId, DRIVER_TRIP_WRONG_BAGGAGE_INFO);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(CREATE_TRIP_PROVIDE_BAGGAGE_INFO);
    }
}
