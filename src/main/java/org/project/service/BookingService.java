package org.project.service;

import org.project.model.Booking;
import org.project.model.TelegramUser;
import org.project.model.Trip;

import java.util.Optional;

public interface BookingService {
    int getNumberOfBookedSeats(Trip trip);

    Optional<Booking> findNewBooking (long passengerId);

    Booking getNewBooking(long passengerId);

    Booking updateNumberOfBookedSeats(Booking booking, int numberOfBookedSeats);

    void updateBookingTrip (Booking booking, Trip trip);

    boolean isNoNewBooking(TelegramUser telegramUser);

    int getAvailableSeats(Trip trip);
}
