package org.project.repository;

import org.project.model.Booking;
import org.project.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b where b.trip = ?1")
    List<Booking> getAllBookingsByTrip(Trip trip);
}
