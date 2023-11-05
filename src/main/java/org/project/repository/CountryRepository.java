package org.project.repository;

import org.project.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query("select c from Country c where c.id <> ?1")
    Page<Country> findCountriesByIdNot(long id, Pageable pageable);

    @Query("select c from Country c where c.id not in ?1")
    Page<Country> findCountriesByIdNotIn(Collection<Long> ids, Pageable pageable);
}
