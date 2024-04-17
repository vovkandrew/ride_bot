package org.project.util;

import org.project.model.Phase;
import org.project.util.enums.HandlerName;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Optional.ofNullable;
import static org.project.util.constants.Constants.*;
import static org.project.util.constants.Patterns.SPLITERATOR_PATTERN;

public class UpdateHelper {
    public static long getTelegramUserIdFromUpdate(Update update) {
        return ofNullable(update.getMessage()).isPresent() ? update.getMessage().getFrom().getId()
                : ofNullable(update.getCallbackQuery()).isPresent() ? update.getCallbackQuery().getFrom().getId()
                : ofNullable(update.getMyChatMember()).isPresent() ? update.getMyChatMember().getFrom().getId()
                : update.getEditedMessage().getFrom().getId();
    }

    public static boolean isUpdateContainsAnyHandler(Update update, HandlerName... handlerNames) {
        if (!update.hasCallbackQuery()) {
            return false;
        }

        String callbackData = update.getCallbackQuery().getData();

        for (HandlerName handlerName : handlerNames) {
            if (callbackData.contains(handlerName.name())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isUpdateContainsHandler(Update update, HandlerName handlerName) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().contains(handlerName.name());
    }

    public static boolean isUpdateContainsHandler(Update update, Phase phase) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().contains(phase.getHandlerName().name());
    }

    public static boolean isUpdateCallbackEqualsHandler(Update update, HandlerName handlerName) {
        if (update.hasCallbackQuery()) {
            String callbackQuery = update.getCallbackQuery().getData();

            if (callbackQuery.contains(DEFAULT_SEPARATOR)) {
                return callbackQuery.split(SPLITERATOR_PATTERN)[DEFAULT_OFFSET].equals(handlerName.name());
            } else {
                return callbackQuery.equals(handlerName.name());
            }
        }

        return false;
    }

    public static boolean isUpdateCallbackEqualsString(Update update, String handlerName) {
        if (update.hasCallbackQuery()) {
            String callbackQuery = update.getCallbackQuery().getData();
            if (callbackQuery.contains(DEFAULT_SEPARATOR)) {
                return callbackQuery.split(SPLITERATOR_PATTERN)[DEFAULT_OFFSET].equals(handlerName);
            } else {
                return callbackQuery.equals(handlerName);
            }
        }

        return false;
    }

    public static String getUserInputFromUpdate(Update update) {
        return update.hasMessage() ? update.getMessage().getText() : EMPTY_STRING;
    }

    public static int getCallbackQueryIdParamFromUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            String[] callbackArgs = update.getCallbackQuery().getData().split(SPLITERATOR_PATTERN);
            if (callbackArgs.length > 1) {
                return Integer.parseInt(callbackArgs[1]);
            }
            return 0;
        }
        return 0;
    }

    public static String getCallbackQueryStringParamFromUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getData().split(SPLITERATOR_PATTERN)[1];
        }

        throw new IllegalArgumentException(DEFAULT_MISSING_CALLBACK_PARAM_EXCEPTION);
    }

    public static int getOffsetParamFromUpdateByHandler(Update update, HandlerName handlerName) {
        return isUpdateContainsHandler(update, handlerName) ? getCallbackQueryIdParamFromUpdate(update) : DEFAULT_OFFSET;
    }

    public static String joinHandlerAndParam(HandlerName handlerName, long param) {
        return handlerName.name() + DEFAULT_SEPARATOR + param;
    }
}
