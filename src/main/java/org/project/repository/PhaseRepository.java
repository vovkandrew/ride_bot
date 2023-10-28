package org.project.repository;

import org.project.model.Phase;
import org.project.util.enums.HandlerName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhaseRepository extends JpaRepository<Phase, Long> {
    Phase getPhaseByHandlerName(HandlerName handlerName);
}
