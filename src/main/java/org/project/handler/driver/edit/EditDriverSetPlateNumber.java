package org.project.handler.driver.edit;

import org.project.model.Driver;
import org.project.model.Phase;
import org.project.model.UserPhase;
import org.project.service.DriverService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.CAR_PLATE_NUMBER_PATTERN;
import static org.project.util.enums.HandlerName.EDITING_PLATE_NUMBER;

@Component
public class EditDriverSetPlateNumber extends EditDriverInfo {
    public EditDriverSetPlateNumber(DriverService driverService) {
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

            sendEditableMessage(userId, PROVIDE_PLATE_NUMBER);

            return;
        }

        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, CAR_PLATE_NUMBER_PATTERN)) {
            Driver driver = getDriverService().updatePlateNumber(userId, userInput);

            editMessage(userId, format(PLATE_NUMBER_PROVIDED, userInput));

            sendDriverInfoAndUpdateUserPhase(userId, driver, userPhase);

            return;
        }

        sendRemovableMessage(userId, joinMessages(PLATE_NUMBER_INVALID, PROVIDE_PLATE_NUMBER));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(EDITING_PLATE_NUMBER);
    }
}
