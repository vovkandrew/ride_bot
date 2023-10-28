package org.project.handler;

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
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.UpdateHelper.isUpdateContainsHandler;
import static org.project.util.constants.Messages.WRONG_ROUTE_DATA_PROVIDED;
import static org.project.util.enums.UserMessageType.EDITABLE;
import static org.project.util.enums.UserMessageType.REMOVABLE;
import static org.springframework.util.StringUtils.hasText;
import static org.telegram.telegrambots.meta.api.methods.ParseMode.HTML;

@Component
public abstract class UpdateHandler {
    public Phase handlerPhase;
    private TelegramWebhookBot webhookBot;
    private UserMessageService userMessageService;
    private UserPhaseService userPhaseService;
    private PhaseService phaseService;
    private static final long EXPIRATION_PERIOD = 86400000L;

    public UpdateHandler(TelegramWebhookBot webhookBot, UserMessageService userMessageService, UserPhaseService userPhaseService,
                         PhaseService phaseService) {
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

    public TelegramWebhookBot getWebhookBot() {
        return webhookBot;
    }

    public UserMessageService getUserMessageService() {
        return userMessageService;
    }

    public UserPhaseService getUserPhaseService() {
        return userPhaseService;
    }

    public PhaseService getPhaseService() {
        return phaseService;
    }

    protected UpdateHandler() {
    }

    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return phaseOptional.isPresent() && phaseOptional.get().equals(handlerPhase);
    }

    public boolean isUpdateContainsHandlerPhase(Update update) {
        return isUpdateContainsHandler(update, handlerPhase.getHandlerName());
    }

    public abstract void handle(UserPhase userPhase, Update update) throws TelegramApiException;

    @PostConstruct
    public void initHandler() {
        this.handlerPhase = null;
    }

    public boolean isCommand(Update update, String command) {
        return update.hasMessage() && update.getMessage().isCommand() && update.getMessage().getText().equals(command);
    }

    public void deleteRemovableMessagesBy(long userId) throws TelegramApiException {
        for (UserMessage userMessage : userMessageService.getAllUserMessagesByUserIdAndType(userId, REMOVABLE)) {
            if (stillValid(userMessage))
                webhookBot.execute(DeleteMessage.builder().chatId(userId).messageId((int) userMessage.getMessageId()).build());
        }
    }

    private boolean stillValid(UserMessage userMessage) {
        return (valueOf(now()).getTime() - userMessage.getCreatedAt().getTime()) < EXPIRATION_PERIOD;
    }

    public void deleteRemovableMessagesAndEraseAllFromRepo(long userId) throws TelegramApiException {
        deleteRemovableMessagesBy(userId);
        getUserMessageService().deleteAllMessagesByUserId(userId);
    }

    public void deleteRemovableMessagesAndEraseRemovableFromRepo(long userId) throws TelegramApiException {
        deleteRemovableMessagesBy(userId);
        getUserMessageService().deleteAllMessagesByUserIdAndType(userId, REMOVABLE);
    }

    public void deleteRemovableMessagesAndEraseEditFromRepo(long userId) throws TelegramApiException {
        deleteRemovableMessagesBy(userId);
        getUserMessageService().deleteAllMessagesByUserIdAndType(userId, EDITABLE);
    }

    public Message sendMessage(long chatId, String text, ReplyKeyboard replyKeyboard) throws TelegramApiException {
        SendMessage message = SendMessage.builder().chatId(chatId).text(text).parseMode(HTML).replyMarkup(replyKeyboard).build();
        return getWebhookBot().execute(message);
    }

    public Message sendMessage(long chatId, String text) throws TelegramApiException {
        return getWebhookBot().execute(SendMessage.builder().chatId(chatId).text(text).parseMode(HTML).build());
    }

    public void editMessage(long chatId, String text) throws TelegramApiException {
        UserMessage userMessage = getUserMessageService().getUserMessageByUserIdAndType(chatId, EDITABLE);
        getWebhookBot().execute(EditMessageText.builder().chatId(chatId).messageId((int) userMessage.getMessageId())
                .parseMode(HTML).text(text).build());
        getUserMessageService().deleteAllMessagesByUserIdAndType(chatId, EDITABLE);

    }

    public void sendRemovableMessage(long chatId, String text) throws TelegramApiException {
        Message removable = sendMessage(chatId, text);
        getUserMessageService().createRemovableMessage(chatId, removable.getMessageId());
    }

    public void sendRemovableMessage(long chatId, String text, ReplyKeyboard replyKeyboard)
            throws TelegramApiException {
        Message removable = sendMessage(chatId, text, replyKeyboard);
        getUserMessageService().createRemovableMessage(chatId, removable.getMessageId());
    }

    public void sendEditableMessage(long chatId, String text) throws TelegramApiException {
        Message removable = sendMessage(chatId, text);
        getUserMessageService().createEditableMessage(chatId, removable.getMessageId());
    }

    public void sendEditableMessage(long chatId, String text, ReplyKeyboard replyKeyboard)
            throws TelegramApiException {
        Message removable = sendMessage(chatId, text, replyKeyboard);
        getUserMessageService().createEditableMessage(chatId, removable.getMessageId());
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
            sendRemovableMessage(getUserIdFromUpdate(update), WRONG_ROUTE_DATA_PROVIDED);

            return true;
        }

        return false;
    }
}
