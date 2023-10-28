package org.project.service.impl;

import lombok.AllArgsConstructor;
import org.project.model.Country;
import org.project.repository.CountryRepository;
import org.project.service.CountryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public Page<Country> findAllCountries(Pageable pageable) {
        return countryRepository.findAll(pageable);
    }

    @Override
    public Country getCountry(long countryId) {
        return countryRepository.getById(countryId);
    }

    @Override
    public Page<Country> findAllCountriesExcept(Pageable pageable, Country country) {
        return countryRepository.findCountriesByIdNot(pageable, country.getId());
    }

    @Override
    public Page<Country> findAllCountriesExcept(Pageable pageable, Country... countries) {
        return countryRepository.findCountriesByIdNotIn(pageable,
                Arrays.stream(countries).map(Country::getId).collect(Collectors.toList()));
    }
}
