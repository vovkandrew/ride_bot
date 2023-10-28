package org.project.repository;

import org.project.model.UserMessage;
import org.project.util.enums.UserMessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
    List<UserMessage> findAllByUserIdIs(long userId);

    UserMessage findUserMessageByUserIdAndFormattingType(long userId, UserMessageType formattingType);

    List<UserMessage> findAllByUserIdAndFormattingType(long userId, UserMessageType formattingType);

    @Transactional
    void deleteAllByUserId(long userId);

    @Transactional
    void deleteAllByUserIdAndFormattingType(long userId, UserMessageType formattingType);
}
