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

import java.util.List;
import java.util.Optional;

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
    public void deleteAllNewDriversRoutes(long telegramUserId) {
        routeRepository.deleteAllByTelegramUserIdAndStatusAndAndUserType(telegramUserId, NEW, DRIVER);
    }

    @Override
    public void deleteAllNewPassengersRoutes(long telegramUserId) {
        routeRepository.deleteAllByTelegramUserIdAndStatusAndAndUserType(telegramUserId, NEW, PASSENGER);
    }

    @Override
    public Route getNewRoute(long telegramUserId) {
        return routeRepository.getRouteByTelegramUserIdAndStatus(telegramUserId, NEW);
    }

    @Override
    public Route getEditingRoute(long telegramUserId) {
        return routeRepository.getRouteByTelegramUserIdAndStatus(telegramUserId, EDITING);
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
    public Page<Route> getAllCreatedRoutes(Pageable pageable, long telegramUserId) {
        return routeRepository.findAllByTelegramUserIdAndStatus(telegramUserId, CREATED, pageable);
    }

    @Override
    public Page<Route> getAllCreatedPassengerRoutes(Pageable pageable, long telegramUserId) {
        return routeRepository.findAllByTelegramUserIdAndStatusAndUserType(telegramUserId, CREATED, PASSENGER, pageable);
    }

    @Override
    public Page<Route> getAllCreatedDriverRoutes(Pageable pageable, long telegramUserId) {
        return routeRepository.findAllByTelegramUserIdAndStatusAndUserType(telegramUserId, CREATED, DRIVER, pageable);
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
    public void updateAllEditing(long telegramUserId) {
        for (Route route : routeRepository.getAllByTelegramUserIdAndStatus(telegramUserId, EDITING)) {
            route.setStatus(CREATED);
            routeRepository.save(route);
        }
    }

    @Override
    public Route getNewPassengerRoute(long telegramUserId) {
        return routeRepository.getRouteByTelegramUserIdAndStatusAndUserType(telegramUserId, NEW, PASSENGER);
    }

    @Override
    public Route getNewDriverRoute(long telegramUserId) {
        return routeRepository.getRouteByTelegramUserIdAndStatusAndUserType(telegramUserId, NEW, DRIVER);
    }

    @Override
    public List<Long> getAllUsersWhoTrackRoute(Route route) {
        return routeRepository.getAllTelegramUsersByStatusAndRouteInfoAndUserType(CREATED, route.getCountryFrom(),
                route.getCityFrom(), route.getCountryTo(), route.getCityTo(), PASSENGER);
    }

    @Override
    public Optional<Route> findDeletedDriverRoute(long telegramUserId, Route route) {
        return routeRepository.findRouteByDetails(telegramUserId, route.getCountryFrom(), route.getCityFrom(),
                route.getCountryTo(), route.getCityTo(), DELETED, DRIVER);
    }
}
