package org.project.service.impl;

import lombok.AllArgsConstructor;
import org.project.model.Route;
import org.project.repository.RouteRepository;
import org.project.service.CityService;
import org.project.service.CountryService;
import org.project.service.RouteService;
import org.project.util.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.project.util.enums.Status.*;
import static org.project.util.enums.UserType.DRIVER;
import static org.project.util.enums.UserType.PASSENGER;

@Service
@AllArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;
    private final CountryService countryService;
    private final CityService cityService;

    @Override
    public Route saveRoute(Route route) {
        return routeRepository.save(route);
    }

    @Override
    public void deleteAllNewDriversRoutes(long userId) {
        routeRepository.deleteAllByTelegramUserIdAndStatusAndAndUserType(userId, NEW, DRIVER);
    }

    @Override
    public void deleteAllNewPassengersRoutes(long userId) {
        routeRepository.deleteAllByTelegramUserIdAndStatusAndAndUserType(userId, NEW, PASSENGER);
    }

    @Override
    public Route getNewRoute(long userId) {
        return routeRepository.getRouteByTelegramUserIdAndStatus(userId, NEW);
    }

    @Override
    public Route getEditingRoute(long userId) {
        return routeRepository.getRouteByTelegramUserIdAndStatus(userId, EDITING);
    }

    @Override
    public Route updateRouteStatus(Route route, Status status) {
        route.setStatus(status);
        return routeRepository.save(route);
    }

    @Override
    public Route updateRouteCityFrom(Route route, long cityId) {
        route.setCityFrom(cityService.getCity(cityId));
        return saveRoute(route);
    }

    @Override
    public Route updateRouteCountryTo(Route route, long countryId) {
        route.setCountryTo(countryService.getCountry(countryId));
        return saveRoute(route);
    }

    @Override
    public Route updateRouteCountryFrom(Route route, long countryId) {
        route.setCountryFrom(countryService.getCountry(countryId));
        return saveRoute(route);
    }

    @Override
    public Route updateRouteCityTo(Route route, long cityId) {
        route.setCityTo(cityService.getCity(cityId));
        return saveRoute(route);
    }

    @Override
    public Page<Route> getAllCreatedRoutes(Pageable pageable, long userId) {
        return routeRepository.findAllByTelegramUserIdAndStatus(userId, CREATED, pageable);
    }

    @Override
    public Page<Route> getAllCreatedPassengerRoutes(Pageable pageable, long userId) {
        return routeRepository.findAllByTelegramUserIdAndStatusAndUserType(userId, CREATED, PASSENGER, pageable);
    }

    @Override
    public void deleteRoute(Route route) {
        routeRepository.delete(route);
    }

    @Override
    public Route getRoute(long routeId) {
        return routeRepository.getById(routeId);
    }

    @Override
    public Route createRoute(Route route) {
        route.setStatus(CREATED);
        return routeRepository.save(route);
    }

    @Override
    public void updateAllEditing(long userId) {
        for (Route route : routeRepository.getAllByTelegramUserIdAndStatus(userId, EDITING)) {
            route.setStatus(CREATED);
            routeRepository.save(route);
        }
    }

    @Override
    public Route getNewPassengerRoute(long userId) {
        return routeRepository.getRouteByTelegramUserIdAndStatusAndUserType(userId, NEW, PASSENGER);
    }

    @Override
    public Route getNewDriverRoute(long userId) {
        return routeRepository.getRouteByTelegramUserIdAndStatusAndUserType(userId, NEW, DRIVER);
    }
}
