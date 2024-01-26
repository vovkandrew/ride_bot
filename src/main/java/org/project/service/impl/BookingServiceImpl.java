package org.project.service.impl;

import lombok.AllArgsConstructor;
import org.project.model.Booking;
import org.project.model.Trip;
import org.project.repository.BookingRepository;
import org.project.service.BookingService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public int getNumberOfBookedSeats(Trip trip) {
        return bookingRepository.getAllBookingsByTrip(trip).stream().map(Booking::getNumberOfBookedSeats)
                .reduce(0, Integer::sum);
    }
}
