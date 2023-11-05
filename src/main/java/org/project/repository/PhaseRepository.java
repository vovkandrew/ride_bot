package org.project.repository;

import org.project.model.Phase;
import org.project.util.enums.HandlerName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, Long> {
    @Query("select p from Phase p where p.handlerName = ?1")
    Phase getPhaseByHandlerName(HandlerName handlerName);
}
