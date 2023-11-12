package org.project.service.impl;

import lombok.AllArgsConstructor;
import org.project.service.UserMessageRemovalScheduledService;
import org.project.service.UserMessageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class UserMessageRemovalScheduledServiceImpl implements UserMessageRemovalScheduledService {
    private final UserMessageService userMessageService;

    @Override
    @Scheduled(timeUnit = TimeUnit.HOURS, fixedDelay = 24)
    public void deleteAllExpiredRemovableMessages() {
        System.out.println("---DELETING ALL EXPIRED REMOVABLE MESSAGES---");
        userMessageService.deleteAllExpiredRemovableMessages();
    }
}
