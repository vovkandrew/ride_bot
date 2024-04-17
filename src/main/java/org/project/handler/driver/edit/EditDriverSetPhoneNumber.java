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
import static org.project.util.Keyboards.getRemoveKeyboard;
import static org.project.util.Keyboards.getShareContactKeyboard;
import static org.project.util.UpdateHelper.isUpdateContainsHandler;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.EDITING_PHONE_NUMBER;

@Component
public class EditDriverSetPhoneNumber extends EditDriverInfo {
    public EditDriverSetPhoneNumber(DriverService driverService) {
        super(driverService);
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return super.isApplicable(phaseOptional, update) || isUpdateContainsHandlerPhase(update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = UpdateHelper.getTelegramUserIdFromUpdate(update);

        if (isUpdateContainsHandler(update, handlerPhase.getHandlerName())) {
            updateUserPhase(userPhase, handlerPhase);

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            sendRemovableMessage(userId, PROVIDE_PHONE_NUMBER, getShareContactKeyboard());

            return;
        }

        if (update.hasMessage() && update.getMessage().hasContact()) {
            String phoneNumber = update.getMessage().getContact().getPhoneNumber();

            Driver driver = getDriverService().updatePhoneNumber(userId, phoneNumber);

            sendMessage(userId, format(PHONE_NUMBER_PROVIDED, phoneNumber), getRemoveKeyboard());

            sendDriverInfoAndUpdateUserPhase(userId, driver, userPhase);

            return;
        }

        sendRemovableMessage(userId, USER_INPUT_INSTEAD_BUTTON_CLICK_PROVIDED, getShareContactKeyboard());
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(EDITING_PHONE_NUMBER);
    }
}
