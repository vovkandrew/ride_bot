package org.project.service;

import org.project.model.Route;
import org.project.model.Trip;
import org.project.util.enums.Currency;
import org.project.util.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TripService {
    Page<Trip> findAllCreatedNonDriverTrips(long driverId, Pageable pageable);

    Optional<Trip> findNewTrip(long driverId);

    Trip getNewTrip(long driverId);

    Trip updateTripRoute(Trip trip, Route route);

    Trip updateTripDepartureDate(Trip trip, String departureDate);

    Trip updateTripDepartureTime(Trip trip, String departureTime);

    Trip updateTripArrivalDate(Trip trip, String arrivalDate);

    Trip updateTripArrivalTime(Trip trip, String arrivalTime);

    Trip updateTripPickupPoint(Trip trip, String pickupPoint);

    Trip updateTripDropOffPoint(Trip trip, String dropOffPoint);

    Trip updateTripCurrency(Trip trip, Currency currency);

    Trip updateTripPrice(Trip trip, String price);

    Trip updateTripBaggageInfo(Trip trip, String baggageInfo);

    Trip updateTripOtherInfo(Trip trip, String otherInfo);

    Trip updateTripStatus(Trip trip, Status status);

    Trip getTrip(long tripId);

    Trip updateAllEditingTrips(Trip trip);

    Trip getFirstEditingTrip(long userId);

    void deleteTrip(long tripId);

    Page<Trip> findAllCreatedNonDriverTrips(Route route, Pageable pageable);

    void deleteAllNewTrips(Route route);

    public boolean isNonExpiredTripsExists(long routeId, String currentDateTime);
}
