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
import static org.project.util.constants.Patterns.CAR_PLATE_NUMBER_PATTERN;
import static org.project.util.enums.HandlerName.GET_PLATE_NUMBER;
import static org.project.util.enums.HandlerName.GET_SEATS_NUMBER;

@Component
public class CreateDriverSetPlateNumber extends UpdateHandler {
    private final DriverService driverService;

    public CreateDriverSetPlateNumber(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);
        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, CAR_PLATE_NUMBER_PATTERN)) {
            driverService.updatePlateNumber(telegramUserId, userInput);

            editMessage(telegramUserId, format(PLATE_NUMBER_PROVIDED, userInput));

            deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

            updateUserPhase(userPhase, GET_SEATS_NUMBER);

            sendEditableMessage(telegramUserId, PROVIDE_SEATS_NUMBER);

            return;
        }

        sendRemovableMessage(telegramUserId, joinMessages(PLATE_NUMBER_INVALID, PROVIDE_CAR_MODEL));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(GET_PLATE_NUMBER);
    }
}
