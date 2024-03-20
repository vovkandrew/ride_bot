package org.project.service.impl;

import lombok.AllArgsConstructor;
import org.project.model.Route;
import org.project.model.Trip;
import org.project.repository.TripRepository;
import org.project.service.TripService;
import org.project.util.enums.Currency;
import org.project.util.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.project.util.constants.Constants.DATE_FORMAT;
import static org.project.util.constants.Constants.TIME_FORMAT;
import static org.project.util.enums.Status.CREATED;
import static org.project.util.enums.Status.EDITING;

@Service
@AllArgsConstructor
public class TripServiceImpl implements TripService {
    private final TripRepository tripRepository;

    @Override
    public Page<Trip> findAllCreatedNonDriverTrips(long driverId, Pageable pageable) {
        return tripRepository.findAllCreated(driverId, pageable);
    }

    @Override
    public Optional<Trip> findNewTrip(long driverId) {
        return tripRepository.findNew(driverId);
    }



    @Override
    public Trip getNewTrip(long driverId) {
        return  tripRepository.findNew(driverId).orElse(new Trip());
    }

    @Override
    public Trip updateTripRoute(Trip trip, Route route) {
        trip.setRoute(route);
        return tripRepository.save(trip);
    }

    @Override
    public Trip updateTripDepartureDate(Trip trip, String departureDate) {
        trip.setDepartureDate(LocalDate.parse(departureDate, DateTimeFormatter.ofPattern(DATE_FORMAT)));
        return tripRepository.save(trip);
    }

    @Override
    public Trip updateTripDepartureTime(Trip trip, String departureTime) {
        trip.setDepartureTime(LocalTime.parse(departureTime, DateTimeFormatter.ofPattern(TIME_FORMAT)));
        return tripRepository.save(trip);
    }

    @Override
    public Trip updateTripArrivalDate(Trip trip, String arrivalDate) {
        trip.setArrivalDate(LocalDate.parse(arrivalDate, DateTimeFormatter.ofPattern(DATE_FORMAT)));
        return tripRepository.save(trip);
    }

    @Override
    public Trip updateTripArrivalTime(Trip trip, String arrivalTime) {
        trip.setArrivalTime(LocalTime.parse(arrivalTime, DateTimeFormatter.ofPattern(TIME_FORMAT)));
        return tripRepository.save(trip);
    }

    @Override
    public Trip updateTripPickupPoint(Trip trip, String pickupPoint) {
        trip.setPickupPoint(pickupPoint);
        return tripRepository.save(trip);
    }

    @Override
    public Trip updateTripDropOffPoint(Trip trip, String dropOffPoint) {
        trip.setDropOffPoint(dropOffPoint);
        return tripRepository.save(trip);
    }

    @Override
    public Trip updateTripCurrency(Trip trip, Currency currency) {
        trip.setCurrency(currency);
        return tripRepository.save(trip);
    }

    @Override
    public Trip updateTripPrice(Trip trip, String price) {
        trip.setPrice(Integer.parseInt(price));
        return tripRepository.save(trip);
    }

    @Override
    public Trip updateTripBaggageInfo(Trip trip, String baggageInfo) {
        trip.setBaggageInfo(baggageInfo);
        return tripRepository.save(trip);
    }

    @Override
    public Trip updateTripOtherInfo(Trip trip, String otherInfo) {
        trip.setOtherInfo(otherInfo);
        return tripRepository.save(trip);
    }

    @Override
    public Trip updateTripStatus(Trip trip, Status status) {
        trip.setStatus(status);
        return tripRepository.save(trip);
    }

    @Override
    public Trip getTrip(long tripId) {
        return tripRepository.getById(tripId);
    }

    @Override
    public Trip updateAllEditingTrips(Trip trip) {
        List<Trip> trips = tripRepository.findAllEditing(trip.getRoute().getTelegramUserId());
        if (!trips.isEmpty()) {
            for (Trip iterable: trips) {
                iterable.setStatus(CREATED);
            }
        }
        tripRepository.saveAll(trips);

        trip.setStatus(EDITING);
        return tripRepository.save(trip);
    }

    @Override
    public Trip getFirstEditingTrip(long userId) {
        return tripRepository.findAllEditing(userId).get(0);
    }

    @Override
    public void deleteTrip(long tripId) {
        tripRepository.deleteById(tripId);
    }

    @Override
    public Page<Trip> findAllCreatedNonDriverTrips(Route route, Pageable pageable) {
        return tripRepository.findAllCreatedNonDriverTripsByRouteDetails(route.getCountryFrom(), route.getCityFrom(), route.getCountryTo(),
                route.getCityTo(), route.getTelegramUserId(), pageable);
    }

    @Override
    public void deleteAllNewTrips(Route route) {
        tripRepository.deleteAllTripsByRouteAndStatus(route, Status.NEW);
    }

    @Override
    public boolean isNonExpiredTripsExists(long routeId, String currentDateTime) {
        return tripRepository.isNonExpired(routeId, currentDateTime);
    }

}
