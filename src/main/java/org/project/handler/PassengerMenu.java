package org.project.handler;

import org.project.model.Phase;
import org.project.model.UserPhase;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static org.project.util.Keyboards.getPassengerMainMenuKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Messages.GREETING_PASSENGER;
import static org.project.util.enums.HandlerName.DECLINE_BOOKING_SEATS;
import static org.project.util.enums.HandlerName.PASSENGER_MENU;

@Component
public class PassengerMenu extends UpdateHandler {

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsAnyHandler(update, handlerPhase.getHandlerName(), DECLINE_BOOKING_SEATS);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);

        updateUserPhase(userPhase, handlerPhase);

        deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

        sendRemovableMessage(telegramUserId, GREETING_PASSENGER, getPassengerMainMenuKeyboard());
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(PASSENGER_MENU);
    }
}
