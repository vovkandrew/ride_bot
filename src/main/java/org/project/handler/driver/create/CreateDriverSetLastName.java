package org.project.handler.driver.create;

import org.project.handler.UpdateHandler;
import org.project.model.UserPhase;
import org.project.service.DriverService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.String.format;
import static org.project.util.Keyboards.getShareContactKeyboard;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.UpdateHelper.getUserInputFromUpdate;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.DRIVER_LAST_NAME;
import static org.project.util.enums.HandlerName.GET_LAST_NAME;
import static org.project.util.enums.HandlerName.GET_PHONE_NUMBER;

@Component
public class CreateDriverSetLastName extends UpdateHandler {
    private final DriverService driverService;

    public CreateDriverSetLastName(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, DRIVER_LAST_NAME)) {
            driverService.updateLastName(userId, userInput);

            editMessage(userId, format(LAST_NAME_PROVIDED, userInput));

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            updateUserPhase(userPhase, GET_PHONE_NUMBER);

            sendEditableMessage(userId, PROVIDE_PHONE_NUMBER, getShareContactKeyboard());

            return;
        }

        sendRemovableMessage(userId, joinMessages(LAST_NAME_INVALID, PROVIDE_LAST_NAME));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(GET_LAST_NAME);
    }
}
