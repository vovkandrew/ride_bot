package org.project.handler.driver.create;

import org.project.handler.UpdateHandler;
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
import static org.project.util.enums.HandlerName.GET_FIRST_NAME;
import static org.project.util.enums.HandlerName.GET_LAST_NAME;

@Component
public class CreateDriverSetFirstName extends UpdateHandler {
    private final DriverService driverService;

    public CreateDriverSetFirstName(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return super.isApplicable(phaseOptional, update) || isUpdateContainsHandler(update, handlerPhase);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);

        if (isUpdateContainsHandler(update, handlerPhase.getHandlerName())) {
            driverService.deleteDriverByIdAndIsFinished(telegramUserId, false);

            updateUserPhase(userPhase, handlerPhase);

            deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

            sendEditableMessage(telegramUserId, PROVIDE_FIRST_NAME);

            return;
        }

        String userInput = getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, FIRST_NAME_PATTERN)) {
            driverService.saveDriver(Driver.builder().id(telegramUserId).firstName(userInput).build());

            editMessage(telegramUserId, format(FIRST_NAME_PROVIDED, userInput));

            deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

            updateUserPhase(userPhase, GET_LAST_NAME);

            sendEditableMessage(telegramUserId, PROVIDE_LAST_NAME);

            return;
        }

        sendRemovableMessage(telegramUserId, joinMessages(FIRST_NAME_INVALID, PROVIDE_FIRST_NAME));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(GET_FIRST_NAME);
    }
}
