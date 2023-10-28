package org.project.service.impl;

import lombok.AllArgsConstructor;
import org.project.model.Phase;
import org.project.model.UserPhase;
import org.project.repository.PhaseRepository;
import org.project.repository.UserPhaseRepository;
import org.project.service.UserPhaseService;
import org.project.util.enums.HandlerName;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserPhaseServiceImpl implements UserPhaseService {
    private final UserPhaseRepository userPhaseRepository;
    private final PhaseRepository phaseRepository;

    @Override
    public void updateUserPhase(UserPhase userPhase, Phase phase) {
        userPhase.setPhase(phase);
        userPhaseRepository.save(userPhase);
    }

    @Override
    public void updateUserPhase(UserPhase userPhase, HandlerName handlerName) {
        updateUserPhase(userPhase, phaseRepository.getPhaseByHandlerName(handlerName));
    }

    @Override
    public Optional<UserPhase> findUserPhaseByUserId(long userId) {
        return userPhaseRepository.findById(userId);
    }

    @Override
    public void deleteUserPhase(UserPhase userPhase) {
        userPhaseRepository.delete(userPhase);
    }
}
