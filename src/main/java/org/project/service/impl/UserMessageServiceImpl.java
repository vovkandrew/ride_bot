package org.project.service.impl;

import lombok.AllArgsConstructor;
import org.project.model.UserMessage;
import org.project.repository.UserMessageRepository;
import org.project.service.UserMessageService;
import org.project.util.enums.UserMessageType;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.sql.Timestamp.valueOf;
import static java.time.LocalDateTime.now;
import static org.project.util.enums.UserMessageType.EDITABLE;
import static org.project.util.enums.UserMessageType.REMOVABLE;

@Service
@AllArgsConstructor
public class UserMessageServiceImpl implements UserMessageService {
    private final UserMessageRepository userMessageRepository;

    @Override
    public void createEditableMessage(long userId, int messageId) {
        userMessageRepository.save(UserMessage.builder().messageId(messageId).userId(userId).formattingType(EDITABLE)
                .createdAt(valueOf(now())).build());
    }

    @Override
    public void createRemovableMessage(long userId, int messageId) {
        userMessageRepository.save(UserMessage.builder().messageId(messageId).userId(userId).formattingType(REMOVABLE)
                .createdAt(valueOf(now())).build());
    }

    @Override
    public void deleteAllMessagesByUserId(long userId) {
        userMessageRepository.deleteAllByUserId(userId);
    }

    @Override
    public void deleteAllMessagesByUserIdAndType(long userId, UserMessageType formattingType) {
        userMessageRepository.deleteAllByUserIdAndFormattingType(userId, formattingType);
    }

    @Override
    public void deleteAllRemovableMessagesByUserId(long userId) {
        userMessageRepository.deleteAllByUserIdAndFormattingType(userId, REMOVABLE);
    }

    @Override
    public UserMessage getUserMessageByUserIdAndType(long userId, UserMessageType formattingType) {
        return userMessageRepository.findUserMessageByUserIdAndFormattingType(userId, formattingType);
    }

    @Override
    public List<UserMessage> getAllUserMessagesByUserIdAndType(long userId, UserMessageType formattingType) {
        return userMessageRepository.findAllByUserIdAndFormattingType(userId, formattingType);
    }

    @Override
    public void deleteAllExpiredRemovableMessages() {
        userMessageRepository.deleteAllExpiredRemovableMessages();
    }
}
