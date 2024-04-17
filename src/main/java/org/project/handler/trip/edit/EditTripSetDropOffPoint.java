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
import static org.project.util.constants.Patterns.getGeneralMessagePatternWithParameterizedStartLimit;
import static org.project.util.enums.HandlerName.DRIVER_TRIP_EDITING_DROPOFF_POINT;
import static org.project.util.enums.Status.CREATED;

@Component
public class EditTripSetDropOffPoint extends EditTripDetails {
    public EditTripSetDropOffPoint(TripService tripService, DriverService driverService,
                                   BookingService bookingService) {
        super(tripService, driverService, bookingService);
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_DROPOFF_POINT)
                || super.isApplicable(phaseOptional, update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);
        updateUserPhase(userPhase, DRIVER_TRIP_EDITING_DROPOFF_POINT);
        Trip trip;

        if (isUpdateContainsHandler(update, DRIVER_TRIP_EDITING_DROPOFF_POINT)) {
            trip = getTripService().getTrip(getCallbackQueryIdParamFromUpdate(update));

            getTripService().updateAllEditingTrips(trip);

            deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

            sendEditableMessage(telegramUserId, format(DRIVER_TRIP_ENTER_DROPOFF_POINT, trip.getRoute().getCityTo()));

            return;
        }


        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, getGeneralMessagePatternWithParameterizedStartLimit(10))) {
            trip = getTripService().getFirstEditingTrip(telegramUserId);

            trip.setStatus(CREATED);

            getTripService().updateTripDropOffPoint(trip, userInput);

            editMessage(telegramUserId, format(DRIVER_TRIP_DROPOFF_POINT_PROVIDED, trip.getDropOffPoint()));

            sendDriverTripDetailsAndUpdateUserPhase(telegramUserId, trip, userPhase);

            return;
        }

        sendRemovableMessage(telegramUserId, DRIVER_TRIP_WRONG_DROPOFF_POINT);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIP_EDITING_DROPOFF_POINT);
    }
}
