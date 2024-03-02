package org.project.handler.trip.book;

import org.project.handler.UpdateHandler;
import org.project.model.*;
import org.project.service.BookingService;
import org.project.service.DriverService;
import org.project.service.TelegramUserService;
import org.project.service.TripService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.getBackButton;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.*;
import static org.project.util.enums.Status.NEW;

@Component
public class PassengerReserveSeats extends UpdateHandler {
    private final TripService tripService;
    private final DriverService driverService;
    private final BookingService bookingService;
    private final TelegramUserService telegramUserService;

    public PassengerReserveSeats(TripService tripService, DriverService driverService, BookingService bookingService, TelegramUserService telegramUserService) {
        this.tripService = tripService;
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

        long tripId = getCallbackQueryIdParamFromUpdate(update);

        TelegramUser telegramUser = telegramUserService.getTelegramUser(userId);
        Trip trip = tripService.getTrip(tripId);

        if(bookingService.findNew(telegramUser.getId()).isEmpty()){
            sendEditableMessage(userId, CHECK_AVAILABLE_SEATS);
            int available = driverService.getDriver(trip.getRoute().getTelegramUserId()).getSeatsNumber() -
                    bookingService.getNumberOfBookedSeats(trip);

            if(available == 0){
                sendRemovableMessage(userId, NO_EMPTY_SEATS_LEFT,getBackButton(FIND_TRIP_MENU));
                return;
            }

            bookingService.updateTripBooking(Booking.builder().telegramUser(telegramUser).status(NEW).build(),trip);
            sendRemovableMessage(userId, joinMessages(format(PASSENGER_SEATS_FOUND,available),PASSENGER_ENTER_SEATS));

            updateUserPhase(userPhase, PASSENGER_SEATS_CONFIRM);

        }else if (bookingService.findNew(telegramUser.getId()).get().getTrip().getId() == tripId){
            sendRemovableMessage(userId, CONTINUE_NEW_BOOKING);

        }else {
            sendEditableMessage(userId, CHECK_AVAILABLE_SEATS);
            int available = driverService.getDriver(trip.getRoute().getTelegramUserId()).getSeatsNumber() -
                    bookingService.getNumberOfBookedSeats(trip);

            if(available == 0){
                sendRemovableMessage(userId, NO_EMPTY_SEATS_LEFT,getBackButton(FIND_TRIP_MENU));
                return;
            }

            bookingService.updateTripBooking(bookingService.getNewBooking(telegramUser.getId()),trip);
            sendRemovableMessage(userId, joinMessages(format(PASSENGER_SEATS_FOUND,available),PASSENGER_ENTER_SEATS));

            updateUserPhase(userPhase, PASSENGER_SEATS_CONFIRM);
        }
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(PASSENGER_RESERVE_SEATS);
    }
}
