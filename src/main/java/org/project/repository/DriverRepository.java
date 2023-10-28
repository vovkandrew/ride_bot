package org.project.repository;

import org.project.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByIdAndFinishedIs(long id, boolean finished);
    @Transactional
    void deleteById(long userId);
    @Transactional
    void deleteByIdAndFinished(long userId, boolean finished);
}
