package org.project.handler.driver.edit;

import org.project.model.Driver;
import org.project.model.Phase;
import org.project.model.UserPhase;
import org.project.service.DriverService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.CAR_COLOR_PATTERN;
import static org.project.util.enums.HandlerName.EDITING_CAR_COLOR;

@Component
public class EditDriverSetCarColor extends EditDriverInfo {
    public EditDriverSetCarColor(DriverService driverService) {
        super(driverService);
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return super.isApplicable(phaseOptional, update) || isUpdateContainsHandlerPhase(update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getTelegramUserIdFromUpdate(update);

        if (isUpdateContainsHandler(update, handlerPhase.getHandlerName())) {
            updateUserPhase(userPhase, handlerPhase);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendEditableMessage(userId, PROVIDE_CAR_COLOR);

            return;
        }

        String userInput = getUserInputFromUpdate(update);

        if (StringUtils.hasText(userInput) && userInput.matches(CAR_COLOR_PATTERN)) {
            Driver driver = getDriverService().updateCarColor(userId, userInput);

            editMessage(userId, format(CAR_COLOR_PROVIDED, userInput));

            sendDriverInfoAndUpdateUserPhase(userId, driver, userPhase);

            return;
        }

        sendRemovableMessage(userId, joinMessages(CAR_COLOR_INVALID, PROVIDE_CAR_COLOR));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(EDITING_CAR_COLOR);
    }
}
