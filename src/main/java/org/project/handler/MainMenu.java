package org.project.handler;

import org.project.model.Phase;
import org.project.model.TelegramUser;
import org.project.model.UserPhase;
import org.project.service.TelegramUserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static org.project.util.Keyboards.getMainMenuKeyboard;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.UpdateHelper.isUpdateCallbackEqualsString;
import static org.project.util.constants.Constants.START_GLOBAL_COMMAND;
import static org.project.util.constants.Messages.GENERAL_GREETING;
import static org.project.util.enums.HandlerName.MAIN_MENU;

@Component
public class MainMenu extends UpdateHandler {
    private final TelegramUserService telegramUserService;

    public MainMenu(TelegramUserService telegramUserService) {
        this.telegramUserService = telegramUserService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isCommand(update, START_GLOBAL_COMMAND) || isUpdateCallbackEqualsString(update, START_GLOBAL_COMMAND)
                || super.isApplicable(phaseOptional, update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        if (!telegramUserService.isTelegramUserExist(userId)) {
            telegramUserService.createTelegramUser(TelegramUser.builder().telegramId(userId).build());
        }

        deleteUserPhase(userPhase);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        sendRemovableMessage(userId, GENERAL_GREETING, getMainMenuKeyboard());
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(MAIN_MENU);
    }
}
