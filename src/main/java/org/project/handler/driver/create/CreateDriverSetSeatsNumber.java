package org.project.handler.driver.create;

import org.project.handler.UpdateHandler;
import org.project.model.UserPhase;
import org.project.service.DriverService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.io.File;

import static java.lang.String.format;
import static org.project.util.Keyboards.getConfirmationKeyboard;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.UpdateHelper.getUserInputFromUpdate;
import static org.project.util.constants.Messages.USER_AGREEMENT;
import static org.project.util.constants.Messages.*;
import static org.project.util.constants.Patterns.CAR_SEATS_NUMBER;
import static org.project.util.enums.HandlerName.*;

@Component
public class CreateDriverSetSeatsNumber extends UpdateHandler {
    private final DriverService driverService;

    @Value("${driver.agreement.file.path}")
    private String driverAgreementFilePath;

    private InputFile agreementFile;

    @PostConstruct
    void init() {
        agreementFile = new InputFile(new File(driverAgreementFilePath));
    }

    public CreateDriverSetSeatsNumber(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        String userInput = getUserInputFromUpdate(update);
        updateUserPhase(userPhase, handlerPhase);

        if (isUserInputMatchesPattern(userInput, CAR_SEATS_NUMBER)) {
            driverService.updateSeatsNumber(userId, userInput);

            editMessage(userId, format(SEATS_NUMBER_PROVIDED, userInput));

            deleteRemovableMessagesAndEraseAllFromRepo(userId);

            updateUserPhase(userPhase, CONFIRM_USER_AGREEMENT);

            sendRemovableMessage(userId, USER_AGREEMENT);

            Message removable = getWebhookBot().execute(SendDocument.builder().chatId(userId)
                    .replyMarkup(getConfirmationKeyboard(CONFIRM_USER_AGREEMENT, DECLINE_USER_AGREEMENT))
                    .document(agreementFile).build());
            getUserMessageService().createRemovableMessage(userId, removable.getMessageId());

            return;
        }

        sendRemovableMessage(userId, joinMessages(SEATS_NUMBER_INVALID, PROVIDE_SEATS_NUMBER));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(GET_SEATS_NUMBER);
    }
}
