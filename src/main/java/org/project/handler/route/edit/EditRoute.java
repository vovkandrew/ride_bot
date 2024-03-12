package org.project.handler.route.edit;

import org.project.handler.UpdateHandler;
import org.project.model.Route;
import org.project.model.UserPhase;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.String.format;
import static org.project.util.Keyboards.getDriverRouteMenuKeyboard;
import static org.project.util.constants.Messages.*;
import static org.project.util.enums.HandlerName.ROUTES_MAIN_MENU;

@Component
public class EditRoute extends UpdateHandler {
    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {

    }

    public void sendRouteDetailsAndUpdateUserPhase(long userId, Route route, UserPhase userPhase)
            throws TelegramApiException {
        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        sendRemovableMessage(userId, joinMessages(format(ROUTE_DATA, route.getFormattedData()), ROUTE_DATA_INFO),
                getDriverRouteMenuKeyboard(route.getId()));

        updateUserPhase(userPhase, ROUTES_MAIN_MENU);
    }
}
