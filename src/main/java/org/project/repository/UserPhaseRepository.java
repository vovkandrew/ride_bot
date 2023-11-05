package org.project.repository;

import org.project.model.UserPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPhaseRepository extends JpaRepository<UserPhase, Long> {
}
