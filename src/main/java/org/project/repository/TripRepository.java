package org.project.repository;

import org.project.model.City;
import org.project.model.Country;
import org.project.model.Route;
import org.project.model.Trip;
import org.project.util.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query(value = "select t from Trip t where t.status = 'NEW' and t.route.telegramUserId = ?1")
    Optional<Trip> findNew(long telegramUserId);

    @Query(value = "select t from Trip t where t.status = 'CREATED' and t.route.telegramUserId = ?1")
    Page<Trip> findAllCreated(long telegramUserId, Pageable pageable);

    @Query(value = "select t from Trip t where t.status = 'EDITING' and t.route.telegramUserId = ?1")
    List<Trip> findAllEditing(long telegramUserId);

    @Query(value = "select t from Trip t where t.status = 'CREATED' and t.route.countryFrom = ?1 " +
            "and t.route.cityFrom = ?2 and t.route.countryTo = ?3 and t.route.cityTo = ?4 " +
            "and t.route.telegramUserId != ?5 order by t.departureDate ASC")
    Page<Trip> findAllCreatedNonDriverTripsByRouteDetails(Country countryFrom, City cityFrom, Country countryTo,
                                                          City cityTo, long driverId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("delete from Trip t where t.route = ?1 and t.status = ?2")
    void deleteAllTripsByRouteAndStatus(Route route, Status status);

    @Query ("select (count(t) > 0) from Trip t where t.route.id = ?1 and concat (t.departureDate, ' ', t.departureTime) > ?2")
    boolean isNonExpired(long routeId, String currentDateTime);

}
