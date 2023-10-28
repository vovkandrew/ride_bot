package org.project.repository;

import org.project.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Page<Country> findCountriesByIdNot(Pageable pageable, long id);
    Page<Country> findCountriesByIdNotIn(Pageable pageable, List<Long> ids);
}
