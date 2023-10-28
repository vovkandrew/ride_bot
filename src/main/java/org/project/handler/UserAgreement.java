package org.project.handler;

import org.project.model.Phase;
import org.project.model.UserPhase;
import org.project.service.DriverService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static org.project.util.Keyboards.getDriverMainMenuKeyboard;
import static org.project.util.Keyboards.getMainMenuKeyboard;
import static org.project.util.UpdateHelper.*;
import static org.project.util.constants.Messages.USER_AGREEMENT_CONFIRMED;
import static org.project.util.constants.Messages.USER_AGREEMENT_DECLINED;
import static org.project.util.enums.HandlerName.*;

@Component
public class UserAgreement extends UpdateHandler {
    private final DriverService driverService;

    public UserAgreement(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandlerPhase(update) || (super.isApplicable(phaseOptional, update)
                && isUpdateContainsAnyHandler(update, CONFIRM_USER_AGREEMENT, DECLINE_USER_AGREEMENT));
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        if (isUpdateContainsHandler(update, CONFIRM_USER_AGREEMENT)) {
            driverService.updateFinished(userId, true);

            updateUserPhase(userPhase, DRIVER_MAIN_MENU);

            sendRemovableMessage(userId, USER_AGREEMENT_CONFIRMED, getDriverMainMenuKeyboard());

            return;
        }

        if (isUpdateContainsHandler(update, DECLINE_USER_AGREEMENT)) {
            driverService.deleteDriverById(userId);

            deleteUserPhase(userPhase);

            sendRemovableMessage(userId, USER_AGREEMENT_DECLINED, getMainMenuKeyboard());
        }
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(USER_AGREEMENT);
    }
}
