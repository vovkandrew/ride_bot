package org.project.handler.trip.create;

import org.project.handler.UpdateHandler;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.TripService;
import org.project.util.enums.Currency;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.String.format;
import static org.project.util.UpdateHelper.getCallbackQueryStringParamFromUpdate;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.CREATE_TRIP_CHOOSE_CURRENCY;
import static org.project.util.enums.HandlerName.CREATE_TRIP_PROVIDE_PRICE;

@Component
public class CreateTripSetCurrency extends UpdateHandler {
    private final TripService tripService;

    public CreateTripSetCurrency(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        Trip trip = tripService.getNewTrip(userId);

        if (!isUserInputPresented(update)) {
            trip = tripService.updateTripCurrency(trip, Currency.valueOf(getCallbackQueryStringParamFromUpdate(update)));

            editMessage(userId, format(DRIVER_TRIP_CURRENCY_PROVIDED, trip.getCurrency().name()));

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendEditableMessage(userId, format(DRIVER_TRIP_ENTER_PRICE, trip.getCurrency().name()));

            updateUserPhase(userPhase, CREATE_TRIP_PROVIDE_PRICE);

            return;
        }

        sendRemovableMessage(userId, USER_INPUT_INSTEAD_BUTTON_CLICK_PROVIDED);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(CREATE_TRIP_CHOOSE_CURRENCY);
    }
}
