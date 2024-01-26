package org.project.handler.trip.edit;

import lombok.Getter;
import org.project.handler.UpdateHandler;
import org.project.model.Driver;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.BookingService;
import org.project.service.DriverService;
import org.project.service.TripService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static java.lang.String.format;
import static org.project.util.Keyboards.getDriverTripDetailsKeyboard;
import static org.project.util.constants.Messages.TRIP_DETAILS;
import static org.project.util.enums.HandlerName.DRIVER_TRIP_DETAILS;
import static org.project.util.enums.HandlerName.DRIVER_TRIP_DETAILS_LESS;

@Getter
@Component
public class EditTripDetails extends UpdateHandler {
    private final TripService tripService;
    private final DriverService driverService;
    private final BookingService bookingService;

    public EditTripDetails(TripService tripService, DriverService driverService, BookingService bookingService) {
        this.tripService = tripService;
        this.driverService = driverService;
        this.bookingService = bookingService;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {

    }

    public void sendDriverTripDetailsAndUpdateUserPhase(long userId, Trip trip, UserPhase userPhase)
            throws TelegramApiException {
        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        Driver driver = driverService.getDriver(userId);

        int numberOfBookedSeats = bookingService.getNumberOfBookedSeats(trip);

        List<Object> tripDetails = trip.getFormattedDataAsList();
        tripDetails.addAll(List.of(driver.getSeatsNumber(), numberOfBookedSeats));

        sendRemovableMessage(userId, format(TRIP_DETAILS, tripDetails.toArray()),
                getDriverTripDetailsKeyboard(trip.getId(), DRIVER_TRIP_DETAILS_LESS));

        updateUserPhase(userPhase, DRIVER_TRIP_DETAILS);
    }
}
