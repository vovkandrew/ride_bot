package org.project.service.impl;

import lombok.AllArgsConstructor;
import org.project.model.TelegramUser;
import org.project.repository.TelegramUserRepository;
import org.project.service.TelegramUserService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TelegramUserServiceImpl implements TelegramUserService {
    private final TelegramUserRepository repository;

    @Override
    public boolean isTelegramUserExist(long telegramId) {
        return repository.findByTelegramId(telegramId).isPresent();
    }

    @Override
    public void createTelegramUser(TelegramUser user) {
        repository.save(user);
    }

    @Override
    public TelegramUser getTelegramUser(long telegramId) {
        return repository.findByTelegramId(telegramId).get();
    }
}
