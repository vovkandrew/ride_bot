package org.project.service;

import org.project.model.Route;
import org.project.util.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RouteService {
    Route saveRoute(Route route);

    void deleteAllNewDriversRoutes(long userId);

    void deleteAllNewPassengersRoutes(long userId);

    Route getNewRoute(long userId);

    Route getEditingRoute(long userId);

    Route updateRouteStatus(Route route, Status status);

    Route updateRouteCityFrom(Route route, long cityId);

    Route updateRouteCountryTo(Route route, long countryId);

    Route updateRouteCountryFrom(Route route, long countryId);

    Route updateRouteCityTo(Route route, long cityId);

    Page<Route> getAllCreatedRoutes(Pageable pageable, long userId);

    Page<Route> getAllCreatedPassengerRoutes(Pageable pageable, long userId);

    Page<Route> getAllCreatedDriverRoutes(Pageable pageable, long userId);

    void deleteRoute(Route route);

    Route getRoute(long routeId);

    Route createRoute(Route route);

    void updateAllEditing(long userId);

    Route getNewPassengerRoute(long userId);

    Route getNewDriverRoute(long userId);

    List<Long> getAllUsersWhoTrackRoute(Route route);
}
