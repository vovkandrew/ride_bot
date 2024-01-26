package org.project.handler.trip.book;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.TripService;
import org.project.util.UpdateHelper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static org.project.util.UpdateHelper.getUserIdFromUpdate;
import static org.project.util.enums.HandlerName.TRACKING_ROUTE_TRIP_DETAILS;

@Component
public class GetNotifiedPassengerTripDetails extends UpdateHandler {
    private final TripService tripService;

    public GetNotifiedPassengerTripDetails(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return isUpdateContainsHandlerPhase(update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);

        Trip trip = tripService.getTrip(UpdateHelper.getCallbackQueryIdParamFromUpdate(update));


    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(TRACKING_ROUTE_TRIP_DETAILS);
    }
}
