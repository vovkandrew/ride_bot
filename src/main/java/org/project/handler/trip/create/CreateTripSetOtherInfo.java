package org.project.handler.trip.create;

import org.project.handler.UpdateHandler;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.DriverService;
import org.project.service.NotificationService;
import org.project.service.TripService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static java.lang.String.format;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static org.project.util.Keyboards.getDriverTripMenuKeyboard;
import static org.project.util.UpdateHelper.getTelegramUserIdFromUpdate;
import static org.project.util.UpdateHelper.getUserInputFromUpdate;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.GENERAL_MESSAGE_PATTERN;
import static org.project.util.enums.HandlerName.CREATE_TRIP_PROVIDE_OTHER_INFO;
import static org.project.util.enums.HandlerName.CREATE_TRIP_REVIEW_DETAILS;
import static org.project.util.enums.Status.CREATED;

@Component
public class CreateTripSetOtherInfo extends UpdateHandler {
    private final DriverService driverService;
    private final TripService tripService;
    private final NotificationService notificationService;

    public CreateTripSetOtherInfo(DriverService driverService, TripService tripService,
                                  NotificationService notificationService) {
        this.driverService = driverService;
        this.tripService = tripService;
        this.notificationService = notificationService;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);

        Trip trip = tripService.getNewTrip(telegramUserId);

        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, GENERAL_MESSAGE_PATTERN)) {
            trip = tripService.updateTripOtherInfo(trip, userInput);

            editMessage(telegramUserId, format(DRIVER_TRIP_OTHER_INFO_PROVIDED, trip.getOtherInfo()));
            deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

            trip = tripService.updateTripStatus(trip, CREATED);

            List<Object> tripDataList = trip.getFormattedDataAsList();
            tripDataList.add(driverService.getDriver(telegramUserId).getSeatsNumber());
            tripDataList.add(0);

            sendRemovableMessage(telegramUserId, joinMessages(TRIP_CREATED, format(TRIP_DETAILS, tripDataList.toArray())),
                    getDriverTripMenuKeyboard(trip.getId()));

            updateUserPhase(userPhase, CREATE_TRIP_REVIEW_DETAILS);

            Trip finalTrip = trip;
            newSingleThreadExecutor().execute(() -> notificationService.notifyPassengersAboutNewCreatedTrip(finalTrip));
            //newSingleThreadScheduledExecutor().schedule()

            return;
        }

        sendRemovableMessage(telegramUserId, DRIVER_TRIP_WRONG_OTHER_INFO);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(CREATE_TRIP_PROVIDE_OTHER_INFO);
    }
}
