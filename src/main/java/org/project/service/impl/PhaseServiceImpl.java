package org.project.service.impl;

import lombok.AllArgsConstructor;
import org.project.model.Phase;
import org.project.repository.PhaseRepository;
import org.project.service.PhaseService;
import org.project.util.enums.HandlerName;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class PhaseServiceImpl implements PhaseService {
    private final PhaseRepository phaseRepository;

    @Override
    public Phase getPhaseByHandlerName(HandlerName handlerName) {
        return phaseRepository.getPhaseByHandlerName(handlerName);
    }

    @PostConstruct
    private void init() {
        for (HandlerName handlerName : HandlerName.values()) {
            if (phaseRepository.getPhaseByHandlerName(handlerName) == null) {
                phaseRepository.save(Phase.builder().handlerName(handlerName).build());
            }
        }
    }
}
