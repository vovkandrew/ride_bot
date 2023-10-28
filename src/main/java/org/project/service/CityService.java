package org.project.service;

import org.project.model.City;
import org.project.model.Country;
import org.project.model.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityService {
    Page<City> findAllCities(Pageable pageable, Country country);

    Page<City> findAllUnusedCitiesFrom(Route route, Pageable pageable);

    Page<City> findAllUnusedCitiesTo(Route route, Pageable pageable);

    City getCity(long id);
}
