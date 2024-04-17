package org.project.handler.trip.edit;

import org.project.model.Phase;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.BookingService;
import org.project.service.DriverService;
import org.project.service.TripService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.PRICE_PATTERN;
import static org.project.util.enums.HandlerName.DRIVER_TRIP_EDITING_PRICE;
import static org.project.util.enums.Status.CREATED;

@Component
public class EditTripSetPrice extends EditTripDetails {
    public EditTripSetPrice(TripService tripService, DriverService driverService, BookingService bookingService) {
        super(tripService, driverService, bookingService);
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_PRICE) || super.isApplicable(phaseOptional, update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);
        updateUserPhase(userPhase, DRIVER_TRIP_EDITING_PRICE);
        Trip trip;

        if (isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_PRICE)) {
            trip = getTripService().getTrip(getCallbackQueryIdParamFromUpdate(update));

            getTripService().updateAllEditingTrips(trip);

            deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

            sendEditableMessage(telegramUserId, format(DRIVER_TRIP_ENTER_PRICE, trip.getCurrency().name()));

            return;
        }

        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, PRICE_PATTERN)) {
            trip = getTripService().getFirstEditingTrip(telegramUserId);

            trip.setStatus(CREATED);

            getTripService().updateTripPrice(trip, userInput);

            editMessage(telegramUserId, format(DRIVER_TRIP_PRICE_PROVIDED, trip.getPrice()));

            sendDriverTripDetailsAndUpdateUserPhase(telegramUserId, trip, userPhase);

            return;
        }

        sendRemovableMessage(telegramUserId, DRIVER_TRIP_WRONG_PRICE);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIP_EDITING_PRICE);
    }
}
