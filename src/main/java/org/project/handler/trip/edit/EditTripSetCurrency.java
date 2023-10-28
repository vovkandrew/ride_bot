package org.project.handler.trip.edit;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.TripService;
import org.project.util.enums.Currency;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.getCurrenciesKeyboard;
import static org.project.util.Keyboards.getDriverTripDetailsKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.*;
import static org.project.util.enums.Status.CREATED;

@Component
public class EditTripSetCurrency extends UpdateHandler {
    private final TripService tripService;

    public EditTripSetCurrency(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_CURRENCY) || super.isApplicable(phaseOptional, update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        updateUserPhase(userPhase, DRIVER_TRIP_EDITING_CURRENCY);
        Trip trip;

        if (isUpdateCallbackEqualsHandler(update, DRIVER_TRIP_EDITING_CURRENCY)) {
            trip = tripService.getTrip(getCallbackQueryIdParamFromUpdate(update));

            tripService.updateAllEditingTrips(trip);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendEditableMessage(userId, DRIVER_TRIP_CHOOSE_CURRENCY, getCurrenciesKeyboard());

            return;
        }

        trip = tripService.getFirstEditingTrip(userId);

        if (!isUserInputPresented(update)) {
            trip.setStatus(CREATED);

            tripService.updateTripCurrency(trip, Currency.valueOf(getCallbackQueryStringParamFromUpdate(update)));

            editMessage(userId, format(DRIVER_TRIP_CURRENCY_PROVIDED, trip.getCurrency()));

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendRemovableMessage(userId, format(TRIP_DETAILS, trip.getFormattedData()),
                    getDriverTripDetailsKeyboard(trip.getId(), DRIVER_TRIP_DETAILS_LESS));

            updateUserPhase(userPhase, DRIVER_TRIP_DETAILS);

            return;
        }

        sendRemovableMessage(userId, USER_INPUT_INSTEAD_BUTTON_CLICK_PROVIDED);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIP_EDITING_CURRENCY);
    }
}
