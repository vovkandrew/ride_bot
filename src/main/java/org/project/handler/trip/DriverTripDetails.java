package org.project.handler.trip;

import org.project.handler.UpdateHandler;
import org.project.model.Driver;
import org.project.model.Phase;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.BookingService;
import org.project.service.DriverService;
import org.project.service.TripService;
import org.project.util.enums.HandlerName;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.project.util.Keyboards.getDriverTripDetailsKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Messages.TRIP_DETAILS;
import static org.project.util.enums.HandlerName.*;

@Component
public class DriverTripDetails extends UpdateHandler {
    private final TripService tripService;
    private final DriverService driverService;
    private final BookingService bookingService;

    public DriverTripDetails(TripService tripService, DriverService driverService, BookingService bookingService) {
        this.tripService = tripService;
        this.driverService = driverService;
        this.bookingService = bookingService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandler(update, DRIVER_TRIP_DETAILS);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        updateUserPhase(userPhase, DRIVER_TRIP_DETAILS);

        int dataParam = getCallbackQueryIdParamFromUpdate(update);

        HandlerName handlerName = isUpdateContainsHandler(update, DRIVER_TRIP_DETAILS_LESS) ? DRIVER_TRIP_DETAILS_MORE
                : DRIVER_TRIP_DETAILS_LESS;

        Trip trip = tripService.getTrip(dataParam);

        Driver driver = driverService.getDriver(userId);

        int numberOfBookedSeats = bookingService.getNumberOfBookedSeats(trip);

        List<Object> collect = Arrays.stream(trip.getFormattedData()).collect(Collectors.toList());
        collect.add(driver.getSeatsNumber());
        collect.add(numberOfBookedSeats);

        sendRemovableMessage(userId, format(TRIP_DETAILS, collect.toArray()),
                getDriverTripDetailsKeyboard(trip.getId(), handlerName));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIP_DETAILS);
    }
}
