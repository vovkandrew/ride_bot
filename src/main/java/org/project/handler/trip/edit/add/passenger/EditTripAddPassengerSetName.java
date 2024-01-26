package org.project.handler.trip.edit.add.passenger;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.UserPhase;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static org.project.util.enums.HandlerName.DRIVER_TRIP_ADDING_PASSENGER_NAME;

@Component
public class EditTripAddPassengerSetName extends UpdateHandler {
    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return super.isApplicable(phaseOptional, update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {

    }

    @Override
    public void initHandler() {
        this.handlerPhase = getPhaseService().getPhaseByHandlerName(DRIVER_TRIP_ADDING_PASSENGER_NAME);
    }
}
