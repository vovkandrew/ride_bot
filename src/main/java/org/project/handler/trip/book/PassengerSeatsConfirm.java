package org.project.handler.trip.book;

import org.project.handler.UpdateHandler;
import org.project.model.*;
import org.project.service.BookingService;
import org.project.service.DriverService;
import org.project.service.TelegramUserService;
import org.project.util.UpdateHelper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.getBackButton;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.UpdateHelper.isUpdateContainsHandler;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Messages.SEATS_NUMBER_INVALID;
import static org.project.util.constants.Patterns.CAR_SEATS_NUMBER_PATTERN;
import static org.project.util.enums.HandlerName.FIND_TRIP_MENU;
import static org.project.util.enums.HandlerName.PASSENGER_SEATS_CONFIRM;

@Component
public class PassengerSeatsConfirm extends UpdateHandler {

    private final DriverService driverService;
    private final BookingService bookingService;
    private final TelegramUserService telegramUserService;

    public PassengerSeatsConfirm(DriverService driverService, BookingService bookingService, TelegramUserService telegramUserService) {
        this.driverService = driverService;
        this.bookingService = bookingService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return super.isApplicable(phaseOptional, update) || isUpdateContainsHandler(update, handlerPhase);
    }
    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        deleteRemovableMessagesAndEraseAllFromRepo(userId);
        updateUserPhase(userPhase, handlerPhase);
        String userInput = UpdateHelper.getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, CAR_SEATS_NUMBER_PATTERN)){
            Booking booking = bookingService.getNewBooking(telegramUserService.getTelegramUser(userId).getId());

            Trip trip = booking.getTrip();
            int seatsInput = Integer.parseInt(userInput);

            int available = driverService.getDriver(trip.getRoute().getTelegramUserId()).getSeatsNumber() -
                    bookingService.getNumberOfBookedSeats(trip);

            if(available == 0){
                sendRemovableMessage(userId, NO_EMPTY_SEATS_LEFT,getBackButton(FIND_TRIP_MENU));
                return;
            }

            if(seatsInput > available){
                sendRemovableMessage(userId, PASSENGER_SEATS_TOO_MUCH);
                return;
            }

            sendRemovableMessage(userId, format(PASSENGER_ENTER_SEATS_PROVIDED, userInput));
            return;
        }
        sendRemovableMessage(userId, SEATS_NUMBER_INVALID);
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(PASSENGER_SEATS_CONFIRM);
    }
}