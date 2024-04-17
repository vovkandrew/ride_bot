package org.project.handler.driver;

import org.project.handler.UpdateHandler;
import org.project.model.Driver;
import org.project.model.Phase;
import org.project.model.UserPhase;
import org.project.service.DriverService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static org.project.util.Keyboards.getAvailableServicesKeyboard;
import static org.project.util.Keyboards.getDriverMainMenuKeyboard;
import static org.project.util.UpdateHelper.getTelegramUserIdFromUpdate;
import static org.project.util.UpdateHelper.isUpdateContainsHandler;
import static org.project.util.constants.Constants.DRIVER_MENU_GLOBAL_COMMAND;
import static org.project.util.constants.Messages.GREETING_DRIVER;
import static org.project.util.constants.Messages.OFFER_REGISTRATION;
import static org.project.util.enums.HandlerName.DRIVER_MENU;

@Component
public class DriverMenu extends UpdateHandler {
    private final DriverService driverService;

    public DriverMenu(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isCommand(update, DRIVER_MENU_GLOBAL_COMMAND) || isUpdateContainsHandler(update, DRIVER_MENU);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long telegramUserId = getTelegramUserIdFromUpdate(update);

        Optional<Driver> driverOptional = driverService.findDriver(telegramUserId);

        deleteRemovableMessagesAndEraseAllFromRepo(telegramUserId);

        updateUserPhase(userPhase, handlerPhase);

        if (driverOptional.isEmpty() || !driverOptional.get().isFinished()) {
            sendRemovableMessage(telegramUserId, OFFER_REGISTRATION, getAvailableServicesKeyboard());

            return;
        }

        sendRemovableMessage(telegramUserId, GREETING_DRIVER, getDriverMainMenuKeyboard());
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_MENU);
    }
}
