package org.project.service;

import org.project.model.Trip;

public interface BookingService {
    int getNumberOfBookedSeats(Trip trip);
}
