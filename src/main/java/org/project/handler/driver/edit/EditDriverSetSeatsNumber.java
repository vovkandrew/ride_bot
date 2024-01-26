package org.project.handler.driver.edit;

import org.project.model.Driver;
import org.project.model.Phase;
import org.project.model.UserPhase;
import org.project.service.DriverService;
import org.project.util.UpdateHelper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.UpdateHelper.isUpdateContainsHandler;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.CAR_SEATS_NUMBER_PATTERN;
import static org.project.util.enums.HandlerName.EDITING_SEATS_NUMBER;

@Component
public class EditDriverSetSeatsNumber extends EditDriverInfo {
    public EditDriverSetSeatsNumber(DriverService driverService) {
        super(driverService);
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return super.isApplicable(phaseOptional, update) || isUpdateContainsHandler(update, handlerPhase);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        if (isUpdateContainsHandler(update, handlerPhase.getHandlerName())) {
            updateUserPhase(userPhase, handlerPhase);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendEditableMessage(userId, PROVIDE_SEATS_NUMBER);

            return;
        }

        String userInput = UpdateHelper.getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, CAR_SEATS_NUMBER_PATTERN)) {
            Driver driver = getDriverService().updateSeatsNumber(userId, userInput);

            editMessage(userId, format(SEATS_NUMBER_PROVIDED, userInput));

            sendDriverInfoAndUpdateUserPhase(userId, driver, userPhase);

            return;
        }

        sendRemovableMessage(userId, joinMessages(SEATS_NUMBER_INVALID, PROVIDE_SEATS_NUMBER));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(EDITING_SEATS_NUMBER);
    }
}
