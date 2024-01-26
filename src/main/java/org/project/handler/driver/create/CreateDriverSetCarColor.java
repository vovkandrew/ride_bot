package org.project.handler.driver.create;

import org.project.handler.UpdateHandler;
import org.project.model.UserPhase;
import org.project.service.DriverService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.String.format;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.UpdateHelper.getUserInputFromUpdate;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.CAR_COLOR_PATTERN;
import static org.project.util.enums.HandlerName.GET_CAR_COLOR;
import static org.project.util.enums.HandlerName.GET_PLATE_NUMBER;
import static org.springframework.util.StringUtils.hasText;

@Component
public class CreateDriverSetCarColor extends UpdateHandler {
    private final DriverService driverService;

    public CreateDriverSetCarColor(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        String userInput = getUserInputFromUpdate(update);

        if (hasText(userInput) && userInput.matches(CAR_COLOR_PATTERN)) {
            driverService.updateCarColor(userId, userInput);

            editMessage(userId, format(CAR_COLOR_PROVIDED, userInput));

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            updateUserPhase(userPhase, GET_PLATE_NUMBER);

            sendEditableMessage(userId, PROVIDE_PLATE_NUMBER);

            return;
        }

        sendRemovableMessage(userId, joinMessages(CAR_COLOR_INVALID, PROVIDE_CAR_COLOR));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(GET_CAR_COLOR);
    }
}
