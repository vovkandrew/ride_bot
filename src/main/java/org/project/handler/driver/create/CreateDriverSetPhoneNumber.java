package org.project.handler.driver.create;

import org.project.handler.UpdateHandler;
import org.project.model.UserPhase;
import org.project.service.DriverService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.String.format;
import static org.project.util.Keyboards.getRemoveKeyboard;
import static org.project.util.Keyboards.getShareContactKeyboard;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.GET_CAR_MODEL;
import static org.project.util.enums.HandlerName.GET_PHONE_NUMBER;

@Component
public class CreateDriverSetPhoneNumber extends UpdateHandler {
    private final DriverService driverService;

    public CreateDriverSetPhoneNumber(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        if (update.hasMessage() && update.getMessage().hasContact()) {
            String phoneNumber = update.getMessage().getContact().getPhoneNumber();

            driverService.updatePhoneNumber(userId, phoneNumber);

            sendMessage(userId, format(PHONE_NUMBER_PROVIDED, phoneNumber), getRemoveKeyboard());

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            updateUserPhase(userPhase, GET_CAR_MODEL);

            sendEditableMessage(userId, PROVIDE_CAR_MODEL);

            return;
        }

        sendRemovableMessage(userId, USER_INPUT_INSTEAD_BUTTON_CLICK_PROVIDED, getShareContactKeyboard());
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(GET_PHONE_NUMBER);
    }
}
