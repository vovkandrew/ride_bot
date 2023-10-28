package org.project.service;

import org.project.model.Phase;
import org.project.model.UserPhase;
import org.project.util.enums.HandlerName;

import java.util.Optional;

public interface UserPhaseService {
    void updateUserPhase(UserPhase userPhase, Phase phase);
    void updateUserPhase(UserPhase userPhase, HandlerName handlerName);
    Optional<UserPhase> findUserPhaseByUserId(long userId);
    void deleteUserPhase(UserPhase userPhase);
}
