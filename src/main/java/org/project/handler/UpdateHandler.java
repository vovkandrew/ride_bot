package org.project.handler;

import lombok.Getter;
import org.project.TelegramWebhookBot;
import org.project.model.Phase;
import org.project.model.UserMessage;
import org.project.model.UserPhase;
import org.project.service.PhaseService;
import org.project.service.UserMessageService;
import org.project.service.UserPhaseService;
import org.project.util.enums.HandlerName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static java.sql.Timestamp.valueOf;
import static java.time.LocalDateTime.now;
import static org.project.util.UpdateHelper.getTelegramUserIdFromUpdate;
import static org.project.util.UpdateHelper.isUpdateContainsHandler;
import static org.project.util.constants.Messages.WRONG_ROUTE_DATA_PROVIDED;
import static org.project.util.enums.UserMessageType.EDITABLE;
import static org.project.util.enums.UserMessageType.REMOVABLE;
import static org.springframework.util.StringUtils.hasText;
import static org.telegram.telegrambots.meta.api.methods.ParseMode.HTML;

@Component
public abstract class UpdateHandler {
    public Phase handlerPhase;
    @Getter
    private TelegramWebhookBot webhookBot;
    @Getter
    private UserMessageService userMessageService;
    @Getter
    private UserPhaseService userPhaseService;
    @Getter
    private PhaseService phaseService;
    private static final long EXPIRATION_PERIOD = 86400000L;

    public UpdateHandler(TelegramWebhookBot webhookBot, UserMessageService userMessageService,
                         UserPhaseService userPhaseService, PhaseService phaseService) {
        this.webhookBot = webhookBot;
        this.userMessageService = userMessageService;
        this.userPhaseService = userPhaseService;
        this.phaseService = phaseService;
    }

    @Autowired
    public final void setWebhookBot(TelegramWebhookBot webhookBot) {
        this.webhookBot = webhookBot;
    }

    @Autowired
    public void setUserMessageService(UserMessageService userMessageService) {
        this.userMessageService = userMessageService;
    }

    @Autowired
    public void setUserPhaseService(UserPhaseService userPhaseService) {
        this.userPhaseService = userPhaseService;
    }

    @Autowired
    public void setPhaseService(PhaseService phaseService) {
        this.phaseService = phaseService;
    }

    protected UpdateHandler() {
    }

    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return phaseOptional.isPresent() && phaseOptional.get().equals(handlerPhase);
    }

    public boolean isUpdateContainsHandlerPhase(Update update) {
        return isUpdateContainsHandler(update, handlerPhase.getHandlerName());
    }

    @Transactional
    public abstract void handle(UserPhase userPhase, Update update) throws TelegramApiException;

    @PostConstruct
    public void initHandler() {
        this.handlerPhase = null;
    }

    public boolean isCommand(Update update, String command) {
        return update.hasMessage() && update.getMessage().isCommand() && update.getMessage().getText().equals(command);
    }

    public void deleteRemovableMessagesBy(long telegramUserId) throws TelegramApiException {
        //forEach instead of stream in order to just throw exception further
        for (UserMessage userMessage : userMessageService.getAllUserMessagesByUserIdAndType(telegramUserId, REMOVABLE)) {
            if (stillValid(userMessage))
                webhookBot.execute(DeleteMessage.builder().chatId(telegramUserId)
                        .messageId((int) userMessage.getMessageId()).build());
        }
    }

    private boolean stillValid(UserMessage userMessage) {
        return (valueOf(now()).getTime() - userMessage.getCreatedAt().getTime()) < EXPIRATION_PERIOD;
    }

    public void deleteRemovableMessagesAndEraseAllFromRepo(long telegramUserId) throws TelegramApiException {
        deleteRemovableMessagesBy(telegramUserId);
        getUserMessageService().deleteAllMessagesByUserId(telegramUserId);
    }

    public void deleteRemovableMessagesAndEraseRemovableFromRepo(long telegramUserId) throws TelegramApiException {
        deleteRemovableMessagesBy(telegramUserId);
        getUserMessageService().deleteAllMessagesByUserIdAndType(telegramUserId, REMOVABLE);
    }

    public void deleteRemovableMessagesAndEraseEditFromRepo(long telegramUserId) throws TelegramApiException {
        deleteRemovableMessagesBy(telegramUserId);
        getUserMessageService().deleteAllMessagesByUserIdAndType(telegramUserId, EDITABLE);
    }

    public Message sendMessage(long telegramUserId, String text, ReplyKeyboard replyKeyboard) throws TelegramApiException {
        return webhookBot.execute(SendMessage.builder().chatId(telegramUserId).text(text).parseMode(HTML)
                .replyMarkup(replyKeyboard).build());
    }

    public Message sendMessage(long telegramUserId, String text) throws TelegramApiException {
        return webhookBot.execute(SendMessage.builder().chatId(telegramUserId).text(text).parseMode(HTML).build());
    }

    public void editMessage(long telegramUserId, String text) throws TelegramApiException {
        UserMessage userMessage = getUserMessageService().getUserMessageByUserIdAndType(telegramUserId, EDITABLE);
        getWebhookBot().execute(EditMessageText.builder().chatId(telegramUserId)
                .messageId((int) userMessage.getMessageId()).parseMode(HTML).text(text).build());
        getUserMessageService().deleteAllMessagesByUserIdAndType(telegramUserId, EDITABLE);

    }

    public void sendRemovableMessage(long telegramUserId, String text) throws TelegramApiException {
        Message removable = sendMessage(telegramUserId, text);
        getUserMessageService().createRemovableMessage(telegramUserId, removable.getMessageId());
    }

    public void sendRemovableMessage(long telegramUserId, String text, ReplyKeyboard replyKeyboard)
            throws TelegramApiException {
        Message removable = sendMessage(telegramUserId, text, replyKeyboard);
        getUserMessageService().createRemovableMessage(telegramUserId, removable.getMessageId());
    }

    public void sendEditableMessage(long telegramUserId, String text) throws TelegramApiException {
        Message removable = sendMessage(telegramUserId, text);
        getUserMessageService().createEditableMessage(telegramUserId, removable.getMessageId());
    }

    public void sendEditableMessage(long telegramUserId, String text, ReplyKeyboard replyKeyboard)
            throws TelegramApiException {
        Message removable = sendMessage(telegramUserId, text, replyKeyboard);
        getUserMessageService().createEditableMessage(telegramUserId, removable.getMessageId());
    }

    public void updateUserPhase(UserPhase userPhase, Phase phase) {
        getUserPhaseService().updateUserPhase(userPhase, phase);
    }

    public void updateUserPhase(UserPhase userPhase, HandlerName handlerName) {
        getUserPhaseService().updateUserPhase(userPhase, handlerName);
    }

    public void deleteUserPhase(UserPhase userPhase) {
        getUserPhaseService().deleteUserPhase(userPhase);
    }

    public boolean isUserInputMatchesPattern(String userInput, String pattern) {
        return hasText(userInput) && userInput.matches(pattern);
    }

    public boolean isUserInputPresented(Update update) {
        return update.hasMessage() && update.getMessage().hasText() && hasText(update.getMessage().getText());
    }

    public boolean isMessageSentInsteadOfButtonClick(Update update) throws TelegramApiException {
        if (update.hasMessage()) {
            sendRemovableMessage(getTelegramUserIdFromUpdate(update), WRONG_ROUTE_DATA_PROVIDED);

            return true;
        }

        return false;
    }
}
