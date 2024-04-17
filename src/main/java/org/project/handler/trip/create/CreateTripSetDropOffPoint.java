package org.project.handler.trip.create;

import org.project.handler.UpdateHandler;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.TripService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.String.format;
import static org.project.util.Keyboards.getCurrenciesKeyboard;
import static org.project.util.UpdateHelper.getTelegramUserIdFromUpdate;
import static org.project.util.UpdateHelper.getUserInputFromUpdate;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.getGeneralMessagePatternWithParameterizedStartLimit;
import static org.project.util.enums.HandlerName.CREATE_TRIP_CHOOSE_CURRENCY;
import static org.project.util.enums.HandlerName.CREATE_TRIP_PROVIDE_DROP_OFF_POINT;

@Component
public class CreateTripSetDropOffPoint extends UpdateHandler {
    private final TripService tripService;

    public CreateTripSetDropOffPoint(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);

        Trip trip = tripService.getNewTrip(telegramUserId);

        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, getGeneralMessagePatternWithParameterizedStartLimit(10))) {
            trip = tripService.updateTripDropOffPoint(trip, userInput);

            editMessage(telegramUserId, format(DRIVER_TRIP_DROPOFF_POINT_PROVIDED, trip.getDropOffPoint()));

            deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

            sendEditableMessage(telegramUserId, DRIVER_TRIP_CHOOSE_CURRENCY, getCurrenciesKeyboard());

            updateUserPhase(userPhase, CREATE_TRIP_CHOOSE_CURRENCY);

            return;
        }

        sendRemovableMessage(telegramUserId, DRIVER_TRIP_WRONG_DROPOFF_POINT);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(CREATE_TRIP_PROVIDE_DROP_OFF_POINT);
    }
}
