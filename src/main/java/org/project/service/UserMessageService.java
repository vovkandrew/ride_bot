package org.project.service;

import org.project.model.UserMessage;
import org.project.util.enums.UserMessageType;

import java.util.List;

public interface UserMessageService {
    void createEditableMessage(long userId, int messageId);
    void createRemovableMessage(long userId, int messageId);
    void deleteAllMessagesByUserId(long userId);
    void deleteAllMessagesByUserIdAndType(long userId, UserMessageType formattingType);
    void deleteAllRemovableMessagesByUserId(long userId);
    UserMessage getUserMessageByUserIdAndType(long userId, UserMessageType formattingType);
    List<UserMessage> getAllUserMessagesByUserIdAndType(long userId, UserMessageType formattingType);
}
