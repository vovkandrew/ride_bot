package org.project.handler.trip.edit;

import org.project.model.Phase;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.BookingService;
import org.project.service.DriverService;
import org.project.service.TripService;
import org.project.util.enums.Currency;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.getCurrenciesKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.DRIVER_TRIP_EDITING_CURRENCY;
import static org.project.util.enums.Status.CREATED;

@Component
public class EditTripSetCurrency extends EditTripDetails {

    public EditTripSetCurrency(TripService tripService, DriverService driverService, BookingService bookingService) {
        super(tripService, driverService, bookingService);
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_CURRENCY) || super.isApplicable(phaseOptional, update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);
        updateUserPhase(userPhase, DRIVER_TRIP_EDITING_CURRENCY);
        Trip trip;

        if (isUpdateCallbackEqualsHandler(update, DRIVER_TRIP_EDITING_CURRENCY)) {
            trip = getTripService().getTrip(getCallbackQueryIdParamFromUpdate(update));

            getTripService().updateAllEditingTrips(trip);

            deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

            sendEditableMessage(telegramUserId, DRIVER_TRIP_CHOOSE_CURRENCY, getCurrenciesKeyboard());

            return;
        }

        if (!isUserInputPresented(update)) {
            trip = getTripService().getFirstEditingTrip(telegramUserId);

            trip.setStatus(CREATED);

            getTripService().updateTripCurrency(trip, Currency.valueOf(getCallbackQueryStringParamFromUpdate(update)));

            editMessage(telegramUserId, format(DRIVER_TRIP_CURRENCY_PROVIDED, trip.getCurrency()));

            sendDriverTripDetailsAndUpdateUserPhase(telegramUserId, trip, userPhase);

            return;
        }

        sendRemovableMessage(telegramUserId, USER_INPUT_INSTEAD_BUTTON_CLICK_PROVIDED);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIP_EDITING_CURRENCY);
    }
}
