package org.project.service.impl;

import lombok.AllArgsConstructor;
import org.project.model.City;
import org.project.model.Country;
import org.project.model.Route;
import org.project.repository.CityRepository;
import org.project.service.CityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Override
    public Page<City> findAllCities(Pageable pageable, Country country) {
        return cityRepository.findAllByCountry(country, pageable);
    }

    @Override
    public Page<City> findAllUnusedCitiesFrom(Route route, Pageable pageable) {
        return cityRepository.findUnusedCitiesFrom(route.getCountryFrom(), route.getTelegramUserId(), route.getCountryFrom(),
                route.getCountryTo(), route.getCityTo(), pageable);
    }

    @Override
    public Page<City> findAllUnusedCitiesTo(Route route, Pageable pageable) {
        return cityRepository.findUnusedCitiesTo(route.getCountryTo(), route.getTelegramUserId(), route.getCountryFrom(),
                route.getCityFrom(), route.getCountryTo(), pageable);
    }

    @Override
    public City getCity(long id) {
        return cityRepository.getById(id);
    }
}
