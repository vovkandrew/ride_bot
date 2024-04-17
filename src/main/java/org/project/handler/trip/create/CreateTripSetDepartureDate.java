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
import static org.project.util.constants.Constants.DATE_FORMAT;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.DATE_PATTERN;
import static org.project.util.enums.HandlerName.CREATE_TRIP_PROVIDE_DEPARTURE_DATE;
import static org.project.util.enums.HandlerName.CREATE_TRIP_PROVIDE_DEPARTURE_TIME;

@Component
public class CreateTripSetDepartureDate extends UpdateHandler {
    private final TripService tripService;

    public CreateTripSetDepartureDate(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);

        Trip trip = tripService.getNewTrip(telegramUserId);

        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, DATE_PATTERN) && trip.verifyDepartureDate(userInput)) {
            trip = tripService.updateTripDepartureDate(trip, userInput);

            editMessage(telegramUserId, format(DRIVER_TRIP_DEPARTURE_DATE_PROVIDED,
                    trip.getDepartureDate().format(ofPattern(DATE_FORMAT))));

            deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

            sendEditableMessage(telegramUserId, DRIVER_TRIP_ENTER_DEPARTURE_TIME);

            updateUserPhase(userPhase, CREATE_TRIP_PROVIDE_DEPARTURE_TIME);

            return;
        }

        sendRemovableMessage(telegramUserId, DRIVER_TRIP_WRONG_DEPARTURE_DATE);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(CREATE_TRIP_PROVIDE_DEPARTURE_DATE);
    }
}
