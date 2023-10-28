package org.project;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.UserPhase;
import org.project.service.UserPhaseService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.constants.Messages.EXCEPTION_MESSAGE;

@Service
public class UpdateDispatcher {
    private final List<UpdateHandler> updateHandlers;
    private final UserPhaseService userPhaseService;
    public UpdateDispatcher(List<UpdateHandler> updateHandlers, UserPhaseService userPhaseService) {
        this.updateHandlers = updateHandlers;
        this.userPhaseService = userPhaseService;
    }

    public BotApiMethod<?> handle(Update update) throws TelegramApiException {
        for (UpdateHandler handler: updateHandlers) {
            long userId = getUserIdFromUpdate(update);
            UserPhase userPhase = userPhaseService.findUserPhaseByUserId(userId)
                    .orElse(UserPhase.builder().userId(userId).build());
            Optional<Phase> optionalPhase = ofNullable(userPhase.getPhase());

            if (handler.isApplicable(optionalPhase, update)) {
                try {
                    handler.handle(userPhase, update);
                } catch (Exception e) {
                    System.out.printf("Error occurred when user %s tried to use handler %s", userId,
                            handler.getClass().getName());
                    System.out.println("\nCause: " + e.getCause());
                    System.out.println("\nMessage: " + e.getMessage());
                    System.out.println("\nStacktrace: ");
                    e.printStackTrace();

                    handler.sendRemovableMessage(userPhase.getUserId(), EXCEPTION_MESSAGE);
                }
            }
        }

        return null;
    }
}
