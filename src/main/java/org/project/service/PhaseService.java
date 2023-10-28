package org.project.service;

import org.project.model.Phase;
import org.project.util.enums.HandlerName;

public interface PhaseService {
    Phase getPhaseByHandlerName(HandlerName handlerName);
}
