package org.project.handler.trip.find;

import org.project.handler.UpdateHandler;
import org.project.model.*;
import org.project.service.*;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.UpdateHelper.*;
import static org.project.util.Keyboards.getPassengerChosenTripDetailsKeyboard;
import static org.project.util.UpdateHelper.getCallbackQueryIdParamFromUpdate;
import static org.project.util.constants.Messages.FIND_TRIP_DETAILS_DESCRIPTION;
import static org.project.util.enums.HandlerName.*;

@Component
public class FindTripDetailsMenu extends UpdateHandler {

    private final TripService tripService;
    private final BookingService bookingService;
    private final DriverService driverService;


    public FindTripDetailsMenu(TripService tripService, BookingService bookingService, DriverService driverService) {
        this.tripService = tripService;
        this.bookingService = bookingService;
        this.driverService = driverService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandlerPhase(update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        deleteRemovableMessagesAndEraseAllFromRepo(userId);
        updateUserPhase(userPhase, handlerPhase);

        if (isMessageSentInsteadOfButtonClick(update)) {
            return;
        }

        long tripId = getCallbackQueryIdParamFromUpdate(update);
        Trip trip = tripService.getTrip(tripId);

        int availableSeats = driverService.getDriver(trip.getRoute().
                getTelegramUserId()).getSeatsNumber()
                - bookingService.getNumberOfBookedSeats(trip);
        Route route = trip.getRoute();

        String msg = format(FIND_TRIP_DETAILS_DESCRIPTION, route.getCountryFrom().getName(), route.getCityFrom().
                        getName(), trip.getPickupPoint(), trip.getFormattedDepartureDate(),
                trip.getFormattedDepartureTime(), route.getCountryTo().getName(), route.getCityTo().getName(),
                trip.getDropOffPoint(), trip.getFormattedArrivalDate(), trip.getFormattedArrivalTime(),
                trip.getBaggageInfo(), trip.getOtherInfo(), trip.getPrice(),trip.getCurrency(), availableSeats);

        sendRemovableMessage(userId, msg,
                getPassengerChosenTripDetailsKeyboard(route.getCityTo().getId(), tripId, FIND_TRIP_CITY_TO));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(FIND_TRIP_MENU_DETAILS);
    }
}
