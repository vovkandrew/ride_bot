package org.project.handler.trip.find;

import org.project.handler.UpdateHandler;
import org.project.model.Phase;
import org.project.model.Route;
import org.project.model.Trip;
import org.project.model.UserPhase;
import org.project.service.CityService;
import org.project.service.CountryService;
import org.project.service.TripService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.lang.String.format;
import static org.project.util.UpdateHelper.*;
import static org.project.util.UpdateHelper.getCallbackQueryIdParamFromUpdate;
import static org.project.util.constants.Messages.FIND_TRIP_DETAILS_DESCRIPTION;
import static org.project.util.enums.HandlerName.*;
import static org.springframework.data.domain.PageRequest.of;

@Component
public class FindTripDetailsMenu extends UpdateHandler {
    private final CountryService countryService;
    private final CityService cityService;
    private final TripService tripService;

    public FindTripDetailsMenu(CountryService countryService, CityService cityService, TripService tripService) {
        this.countryService = countryService;
        this.cityService = cityService;
        this.tripService = tripService;
    }

    @Override
    public boolean isApplicable(Optional<Phase> phaseOptional, Update update) {
        return super.isApplicable(phaseOptional, update) || isUpdateContainsHandlerPhase(update);
    }

    @Override
    public void handle(UserPhase userPhase, Update update) throws TelegramApiException {
        long userId = getUserIdFromUpdate(update);
        updateUserPhase(userPhase, handlerPhase);

        if (isMessageSentInsteadOfButtonClick(update)) {
            return;
        }

        Trip trip = tripService.getTrip(getCallbackQueryIdParamFromUpdate(update));

        Route route = trip.getRoute();

        deleteRemovableMessagesAndEraseAllFromRepo(userId);

        String msg = format(FIND_TRIP_DETAILS_DESCRIPTION, route.getCountryFrom(), route.getCityFrom(), trip.getPickupPoint(),
                trip.getFormattedDepartureDate(), trip.getFormattedDepartureTime(), route.getCountryTo(), route.getCityTo(),
                trip.getDropOffPoint(), trip.getFormattedArrivalDate(), trip.getFormattedArrivalTime(), trip.getBaggageInfo(),
                trip.getOtherInfo(), trip.getPrice(), "0");

//        sendRemovableMessage(userId, msg, getAvailableCountriesKeyboard(
//                countryService.findAllCountriesExcept(pageRequest, route.getCountryFrom()),
//                FIND_TRIP_COUNTRY_TO_NEXT, FIND_TRIP_COUNTRY_TO));
    }

    @Override
    public void initHandler() {
        handlerPhase = getPhaseService().getPhaseByHandlerName(FIND_TRIP_MENU_DETAILS);
    }
}
