package org.project.service.impl;

import lombok.AllArgsConstructor;
import org.project.model.Booking;
import org.project.model.TelegramUser;
import org.project.model.Trip;
import org.project.repository.BookingRepository;
import org.project.repository.DriverRepository;
import org.project.service.BookingService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
	private final BookingRepository bookingRepository;
	private final DriverRepository driverRepository;

	@Override
	public int getNumberOfBookedSeats(Trip trip) {
		return bookingRepository.getAllBookingsByTrip(trip).stream().map(Booking :: getNumberOfBookedSeats)
				.reduce(0, Integer :: sum);
	}

	@Override
	public Optional<Booking> findNewBooking(long passengerId) {
		return bookingRepository.findNewBookingByUserId(passengerId);
	}

	@Override
	public Booking getNewBooking(long passengerId) {
		return bookingRepository.findNewBookingByUserId(passengerId).orElse(new Booking());
	}

	@Override
	public Booking updateNumberOfBookedSeats(Booking booking, int numberOfBookedSeats) {
		booking.setNumberOfBookedSeats(numberOfBookedSeats);
		return bookingRepository.save(booking);
	}

	@Override
	public void updateBookingTrip(Booking booking, Trip trip) {
		booking.setTrip(trip);
		bookingRepository.save(booking);
	}

	@Override
	public boolean hasNewBooking(TelegramUser telegramUser) {
		return bookingRepository.findNewBookingByUserId(telegramUser.getId()).isPresent();
	}

	@Override
	public int getAvailableSeats(Trip trip) {
		return driverRepository.getById(trip.getRoute().getTelegramUserId()).getSeatsNumber()
				- getNumberOfBookedSeats(trip);
	}

}
