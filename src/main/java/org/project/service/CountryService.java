package org.project.service;

import org.project.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CountryService {
    Page<Country> findAllCountries(Pageable pageable);

    Country getCountry(long countryId);

    Page<Country> findAllCountriesExcept(Pageable pageable, Country country);

    Page<Country> findAllCountriesExcept(Pageable pageable, Country... countries);
}
