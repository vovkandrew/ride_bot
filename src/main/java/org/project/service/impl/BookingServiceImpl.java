package org.project.service.impl;

import lombok.AllArgsConstructor;
import org.project.model.Booking;
import org.project.model.Trip;
import org.project.repository.BookingRepository;
import org.project.repository.TripRepository;
import org.project.service.BookingService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;

    @Override
    public int getNumberOfBookedSeats(Trip trip) {
        return bookingRepository.getAllBookingsByTrip(trip).stream().map(Booking::getNumberOfBookedSeats)
                .reduce(0, Integer::sum);
    }

    @Override
    public Optional<Booking> findNew(long passengerId) {
        return bookingRepository.findNew(passengerId);
    }

    @Override
    public Booking getNewBooking(long passengerId) {
        return bookingRepository.findNew(passengerId).orElse(new Booking());
    }

    @Override
    public Booking updateNumberOfBookedSeats(Booking booking, int numberOfBookedSeats) {
        booking.setNumberOfBookedSeats(numberOfBookedSeats);
        return bookingRepository.save(booking);
    }

    @Override
    public void updateTripBooking(Booking booking, Trip trip) {
        booking.setTrip(trip);
        bookingRepository.save(booking);
    }
}
