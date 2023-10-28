package org.project.handler;

import org.project.model.Phase;
import org.project.model.UserPhase;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static org.project.util.Keyboards.getPassengerMainMenuKeyboard;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.UpdateHelper.isUpdateCallbackEqualsHandler;
import static org.project.util.constants.Messages.GREETING_PASSENGER;
import static org.project.util.enums.HandlerName.PASSENGER_MENU;

@Component
public class PassengerMenu extends UpdateHandler {

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateCallbackEqualsHandler(update, handlerPhase.getHandlerName());
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        updateUserPhase(userPhase, handlerPhase);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        sendRemovableMessage(userId, GREETING_PASSENGER, getPassengerMainMenuKeyboard());
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(PASSENGER_MENU);
    }
}
