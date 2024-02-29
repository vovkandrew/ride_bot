package org.project.service;

import org.project.model.Booking;
import org.project.model.Trip;

import java.util.Optional;

public interface BookingService {
    int getNumberOfBookedSeats(Trip trip);

    Optional<Booking> findNew(long passengerId);

    Booking getNewBooking(long passengerId);

    Booking updateNumberOfBookedSeats(Booking booking, int numberOfBookedSeats);

    void updateTripBooking(Booking booking, Trip trip);
}
