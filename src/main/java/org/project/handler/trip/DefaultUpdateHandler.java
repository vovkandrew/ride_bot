package org.project.handler.trip;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.UserPhase;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Component
public class DefaultUpdateHandler extends UpdateHandler {
    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return false;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {

    }
}
