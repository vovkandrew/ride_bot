package org.project.repository;

import org.project.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    @Query("select t from TelegramUser t where t.telegramId = ?1")
    Optional<TelegramUser> findByTelegramId(long telegramId);
}
