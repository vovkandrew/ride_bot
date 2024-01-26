package org.project.handler.driver.edit;

import lombok.Getter;
import org.project.handler.UpdateHandler;
import org.project.model.Driver;
import org.project.model.UserPhase;
import org.project.service.DriverService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.String.format;
import static org.project.util.Keyboards.getEditDriverDetailsKeyboard;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.DRIVER_INFO;

@Getter
@Component
public class EditDriverInfo extends UpdateHandler {
    private final DriverService driverService;

    public EditDriverInfo(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {

    }

    public void sendDriverInfoAndUpdateUserPhase(long userId, Driver driver, UserPhase userPhase)
            throws TelegramApiException {
        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        sendRemovableMessage(userId, joinMessages(DATA_UPDATED, format(DRIVER_DETAILS, driver.getFormattedData())),
                getEditDriverDetailsKeyboard());

        updateUserPhase(userPhase, DRIVER_INFO);
    }

}
