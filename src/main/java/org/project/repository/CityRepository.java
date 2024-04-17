package org.project.repository;

import org.project.model.City;
import org.project.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    @Query("select c from City c where c.country = ?1")
    Page<City> findAllByCountry(Country country, Pageable pageable);

    @Query(value = "select c from City c where c.country = ?1 and c.id not in (select r.cityTo.id from Route r " +
            "where r.telegramUserId = ?2 and r.countryFrom = ?3 and r.cityFrom = ?4 and r.countryTo = ?5 " +
            "and r.status = 'CREATED') order by c.name asc")
    Page<City> findUnusedCitiesTo(Country country, long telegramUserId, Country countryFrom, City cityFrom,
                                  Country countryTo, Pageable pageable);

    @Query(value = "select c from City c where c.country = ?1 and c.id not in (select r.cityFrom.id from Route r " +
            "where r.telegramUserId = ?2 and r.countryFrom = ?3 and r.countryTo = ?4 and r.cityTo = ?5 " +
            "and r.status = 'CREATED') order by c.name asc")
    Page<City> findUnusedCitiesFrom(Country country, long telegramUserId, Country countryFrom, Country countryTo,
                                    City cityTo, Pageable pageable);
}
