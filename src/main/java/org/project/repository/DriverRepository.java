package org.project.repository;

import org.project.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    @Query("select d from Driver d where d.id = ?1 and d.finished = ?2")
    Optional<Driver> findByIdAndFinishedIs(long id, boolean finished);

    @Query("select d from Driver d where d.id = ?1")
    Optional<Driver> findDriverById(long id);

    @Transactional
    @Modifying
    @Query("delete from Driver d where d.id = ?1")
    void deleteById(long id);

    @Transactional
    @Modifying
    @Query("delete from Driver d where d.id = ?1 and d.finished = ?2")
    void deleteByIdAndFinished(long id, boolean finished);
}
