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
import static org.project.util.constants.Patterns.FIRST_NAME_PATTERN;
import static org.project.util.enums.HandlerName.EDITING_FIRST_NAME;

@Component
public class EditDriverSetFirstName extends EditDriverInfo {
    public EditDriverSetFirstName(DriverService driverService) {
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
            getUserPhaseService().updateUserPhase(userPhase, handlerPhase);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendEditableMessage(userId, PROVIDE_FIRST_NAME);

            return;
        }

        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, FIRST_NAME_PATTERN)) {
            Driver driver = getDriverService().updateFirstName(userId, userInput);

            editMessage(userId, format(FIRST_NAME_PROVIDED, userInput));

            sendDriverInfoAndUpdateUserPhase(userId, driver, userPhase);

            return;
        }

        sendRemovableMessage(userId, joinMessages(FIRST_NAME_INVALID, PROVIDE_FIRST_NAME));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(EDITING_FIRST_NAME);
    }
}
