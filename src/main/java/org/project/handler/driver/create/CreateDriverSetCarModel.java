package org.project.handler.driver.create;

import org.project.handler.UpdateHandler;
import org.project.model.UserPhase;
import org.project.service.DriverService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.String.format;
import static org.project.util.UpdateHelper.getTelegramUserIdFromUpdate;
import static org.project.util.UpdateHelper.getUserInputFromUpdate;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.CAR_MODEL_PATTERN;
import static org.project.util.enums.HandlerName.GET_CAR_COLOR;
import static org.project.util.enums.HandlerName.GET_CAR_MODEL;
import static org.springframework.util.StringUtils.hasText;

@Component
public class CreateDriverSetCarModel extends UpdateHandler {
    private final DriverService driverService;

    public CreateDriverSetCarModel(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);
        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, CAR_MODEL_PATTERN)) {
            driverService.updateCarModel(telegramUserId, userInput);

            editMessage(telegramUserId, format(CAR_MODEL_PROVIDED, userInput));

            deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

            updateUserPhase(userPhase, GET_CAR_COLOR);

            sendEditableMessage(telegramUserId, PROVIDE_CAR_COLOR);

            return;
        }

        sendRemovableMessage(telegramUserId, joinMessages(CAR_MODEL_INVALID, PROVIDE_CAR_MODEL));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(GET_CAR_MODEL);
    }
}
