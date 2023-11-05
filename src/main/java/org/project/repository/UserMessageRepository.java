package org.project.repository;

import org.project.model.UserMessage;
import org.project.util.enums.UserMessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
    @Query("select u from UserMessage u where u.userId = ?1")
    List<UserMessage> findAllByUserIdIs(long userId);

    @Query("select u from UserMessage u where u.userId = ?1 and u.formattingType = ?2")
    UserMessage findUserMessageByUserIdAndFormattingType(long userId, UserMessageType formattingType);

    @Query("select u from UserMessage u where u.userId = ?1 and u.formattingType = ?2")
    List<UserMessage> findAllByUserIdAndFormattingType(long userId, UserMessageType formattingType);

    @Transactional
    @Modifying
    @Query("delete from UserMessage u where u.userId = ?1")
    void deleteAllByUserId(long userId);

    @Transactional
    @Modifying
    @Query("delete from UserMessage u where u.userId = ?1 and u.formattingType = ?2")
    void deleteAllByUserIdAndFormattingType(long userId, UserMessageType formattingType);
}
