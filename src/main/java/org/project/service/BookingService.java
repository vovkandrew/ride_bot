package org.project.service;

import org.project.model.Booking;
import org.project.model.TelegramUser;
import org.project.model.Trip;

public interface BookingService {
    int getNumberOfBookedSeats(Trip trip);

    Booking getNewBooking(long passengerId);

    Booking updateNumberOfBookedSeats(Booking booking, int numberOfBookedSeats);

    void updateBookingTrip (Booking booking, Trip trip);

    boolean hasNewBooking(TelegramUser telegramUser);

    int getAvailableSeats(Trip trip);

    Booking getBooking(long bookingId);

    Booking update(Booking booking);

    void deleteAllNewPassengerBookings(long telegramUserId);

    void deleteBooking(Booking booking);
}
