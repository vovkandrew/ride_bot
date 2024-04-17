package org.project.handler.driver;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.UserPhase;
import org.project.service.DriverService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.Keyboards.getEditDriverDetailsKeyboard;
import static org.project.util.UpdateHelper.getTelegramUserIdFromUpdate;
import static org.project.util.UpdateHelper.isUpdateContainsHandler;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.DRIVER_INFO;

@Component
public class DriverDetails extends UpdateHandler {
    private final DriverService driverService;

    public DriverDetails(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandler(update, DRIVER_INFO);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);

        deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

        updateUserPhase(userPhase, DRIVER_INFO);

        sendRemovableMessage(telegramUserId, format(joinMessages(DRIVER_DETAILS, EDIT_INFO),
                driverService.getDriver(telegramUserId).getFormattedData()), getEditDriverDetailsKeyboard());
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_INFO);
    }
}
