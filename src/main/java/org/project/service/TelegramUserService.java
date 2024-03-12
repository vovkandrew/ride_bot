package org.project.service;

import org.project.model.TelegramUser;

public interface TelegramUserService {
    boolean isTelegramUserExist(long telegramId);

    void createTelegramUser(TelegramUser user);

    TelegramUser getTelegramUser(long telegramId);
}
