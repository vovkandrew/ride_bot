package org.project;

import org.project.handler.UpdateHandler;
import org.project.handler.trip.DefaultUpdateHandler;
import org.project.model.UserPhase;
import org.project.service.UserPhaseService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.project.util.UpdateHelper.getTelegramUserIdFromUpdate;
import static org.project.util.constants.Messages.EXCEPTION_MESSAGE;

@Service
public class UpdateDispatcher {
    private final List<UpdateHandler> updateHandlers;
    private final UserPhaseService userPhaseService;
    private final DefaultUpdateHandler defaultUpdateHandler;

    public UpdateDispatcher(List<UpdateHandler> updateHandlers, UserPhaseService userPhaseService,
                            DefaultUpdateHandler defaultUpdateHandler) {
        this.updateHandlers = updateHandlers;
        this.userPhaseService = userPhaseService;
        this.defaultUpdateHandler = defaultUpdateHandler;
    }

    public BotApiMethod<?> handle(Update update) throws TelegramApiException {
        long userId = getTelegramUserIdFromUpdate(update);

        try {
            UserPhase userPhase = userPhaseService.findUserPhaseByUserId(userId)
                    .orElse(UserPhase.builder().userId(userId).build());

            for (UpdateHandler handler : updateHandlers) {
                if (handler.isApplicable(ofNullable(userPhase.getPhase()), update)) {
                    handler.handle(userPhase, update);

                    break;
                }
            }
        } catch (Exception e) {
            System.out.printf("Error occurred with user %s", userId);
            System.out.println("\nCause: " + e.getCause());
            System.out.println("Message: " + e.getMessage());

            defaultUpdateHandler.sendRemovableMessage(userId, EXCEPTION_MESSAGE);
        }

        return null;
    }
}
