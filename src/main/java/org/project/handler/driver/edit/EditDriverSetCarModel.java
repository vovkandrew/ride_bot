package org.project.handler.driver.edit;

import org.project.handler.UpdateHandler;
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
import static org.project.util.Keyboards.getEditDriverDetailsKeyboard;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.UpdateHelper.isUpdateContainsHandler;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.CAR_MODEL;
import static org.project.util.enums.HandlerName.DRIVER_INFO;
import static org.project.util.enums.HandlerName.EDITING_CAR_MODEL;

@Component
public class EditDriverSetCarModel extends UpdateHandler {
    private final DriverService driverService;

    public EditDriverSetCarModel(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return super.isApplicable(phaseOptional, update) || isUpdateContainsHandlerPhase(update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        if (isUpdateContainsHandler(update, handlerPhase.getHandlerName())) {
            updateUserPhase(userPhase, handlerPhase);

            deleteRemovableMessagesAndEraseRemovableFromRepo(userId);

            sendEditableMessage(userId, PROVIDE_CAR_MODEL);

            return;
        }

        String userInput = UpdateHelper.getUserInputFromUpdate(update);

        if (isUserInputMatchesPattern(userInput, CAR_MODEL)) {
            Driver driver = driverService.updateCarModel(userId, userInput);

            editMessage(userId, format(CAR_MODEL_PROVIDED, userInput));

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            updateUserPhase(userPhase, DRIVER_INFO);

            sendRemovableMessage(userId, joinMessages(DATA_UPDATED, format(DRIVER_DETAILS, driver.getFormattedData())),
                    getEditDriverDetailsKeyboard());

            return;
        }

        sendRemovableMessage(userId, joinMessages(CAR_MODEL_INVALID, PROVIDE_CAR_MODEL));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(EDITING_CAR_MODEL);
    }
}
