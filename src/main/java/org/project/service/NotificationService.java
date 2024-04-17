package org.project.service;

import org.project.model.Booking;
import org.project.model.Trip;

public interface NotificationService {
    void notifyPassengersAboutNewCreatedTrip(Trip trip);

    void notifyPassengerAboutSuccessfulPayment(Booking booking);

    void notifyDriverAboutBookedSeatsForHisTrip(Booking booking);

    void notifyPassengerAboutExpiredPaymentLink(Booking booking);
}
