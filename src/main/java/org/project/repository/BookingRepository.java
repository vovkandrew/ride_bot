package org.project.repository;

import org.project.model.Booking;
import org.project.model.TelegramUser;
import org.project.model.Trip;
import org.project.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b where b.trip = ?1")
    List<Booking> getAllBookingsByTrip(Trip trip);

    @Query(value = "select b from Booking b where b.status = 'NEW' and b.telegramUser.telegramId = ?1")
    Optional<Booking> findNewBookingByUserId (long telegramUserId);

    @Transactional
    @Modifying
    @Query("delete from Booking b where b.telegramUser.telegramId = ?1 and b.status = 'NEW'")
    void deleteAllNewPassengerBookings(long telegramUserId);

    @Transactional
    @Modifying
    @Query("delete from Booking b where b.status = 'NEW' and b.telegramUser = ?1")
    void deleteByStatusAndTelegramUser(TelegramUser telegramUser);
}
