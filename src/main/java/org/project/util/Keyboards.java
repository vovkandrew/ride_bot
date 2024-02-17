package org.project.util;

import org.project.model.City;
import org.project.model.Country;
import org.project.model.Route;
import org.project.model.Trip;
import org.project.util.enums.HandlerName;
import org.springframework.data.domain.Page;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.List.of;
import static org.project.util.UpdateHelper.joinHandlerAndParam;
import static org.project.util.constants.Buttons.MAIN_MENU;
import static org.project.util.constants.Buttons.TRACKING_ROUTES_MENU;
import static org.project.util.constants.Buttons.*;
import static org.project.util.constants.Constants.*;
import static org.project.util.enums.Currency.EUR;
import static org.project.util.enums.Currency.UA;
import static org.project.util.enums.HandlerName.*;

public class Keyboards {
    public static List<List<InlineKeyboardButton>> getEditDriverInfoKeyboard() {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        firstRow.add(InlineKeyboardButton.builder().text(EDIT_FIRST_NAME).callbackData(EDITING_FIRST_NAME.name()).build());
        firstRow.add(InlineKeyboardButton.builder().text(EDIT_LAST_NAME).callbackData(EDITING_LAST_NAME.name()).build());

        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        secondRow.add(InlineKeyboardButton.builder().text(EDIT_PHONE_NUMBER).callbackData(EDITING_PHONE_NUMBER.name()).build());
        secondRow.add(InlineKeyboardButton.builder().text(EDIT_CAR_MODEL).callbackData(EDITING_CAR_MODEL.name()).build());

        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        thirdRow.add(InlineKeyboardButton.builder().text(EDIT_CAR_COLOR).callbackData(EDITING_CAR_COLOR.name()).build());
        thirdRow.add(InlineKeyboardButton.builder().text(EDIT_PLATE_NUMBER).callbackData(EDITING_PLATE_NUMBER.name()).build());

        List<InlineKeyboardButton> fourthRow = new ArrayList<>();
        fourthRow.add(InlineKeyboardButton.builder().text(EDIT_SEATS_NUMBER).callbackData(EDITING_SEATS_NUMBER.name()).build());

        return of(firstRow, secondRow, thirdRow, fourthRow);
    }

    public static InlineKeyboardMarkup getEditDriverDetailsKeyboard() {
        List<List<InlineKeyboardButton>> editDriverInfoKeyboard = new ArrayList<>(getEditDriverInfoKeyboard());

        editDriverInfoKeyboard.add(
                of(InlineKeyboardButton.builder().text(BACK_TO_DRIVER_MENU).callbackData(DRIVER_MENU.name()).build()));

        return new InlineKeyboardMarkup(editDriverInfoKeyboard);
    }

    public static InlineKeyboardMarkup confirmRegistration() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(getEditDriverInfoKeyboard());

        List<InlineKeyboardButton> confirmButtonRow = new ArrayList<>();
        confirmButtonRow.add(InlineKeyboardButton.builder().text(CONTINUE_REGISTRATION)
                .callbackData(USER_AGREEMENT.name()).build());
        keyboard.add(confirmButtonRow);

        return new InlineKeyboardMarkup(keyboard);
    }

    public static ReplyKeyboardMarkup getShareContactKeyboard() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow keyboardButtons = new KeyboardRow();
        keyboardButtons.add(KeyboardButton.builder().requestContact(true).text(PROVIDE_YOUR_PHONE_NUMBER).build());
        keyboardRows.add(keyboardButtons);

        return ReplyKeyboardMarkup.builder().oneTimeKeyboard(true).resizeKeyboard(true).keyboard(keyboardRows).build();
    }

    public static InlineKeyboardMarkup getConfirmationKeyboard(HandlerName agree, HandlerName decline) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();

        firstRow.add(InlineKeyboardButton.builder().text(DECLINE).callbackData(decline.name()).build());
        firstRow.add(InlineKeyboardButton.builder().text(CONFIRM).callbackData(agree.name()).build());

        return new InlineKeyboardMarkup(of(firstRow));
    }

    public static InlineKeyboardMarkup getConfirmationKeyboard(String agree, String decline) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();

        firstRow.add(InlineKeyboardButton.builder().text(DECLINE).callbackData(decline).build());
        firstRow.add(InlineKeyboardButton.builder().text(CONFIRM).callbackData(agree).build());

        return new InlineKeyboardMarkup(of(firstRow));
    }

    public static InlineKeyboardMarkup getDriverMainMenuKeyboard() {

        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        firstRow.add(InlineKeyboardButton.builder().text(MY_ROUTS).callbackData(DRIVER_ROUTES.name()).build());

        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        secondRow.add(InlineKeyboardButton.builder().text(MY_TRIPS).callbackData(DRIVER_TRIPS.name()).build());

        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        thirdRow.add(InlineKeyboardButton.builder().text(DRIVER_PERSONAL_DATA).callbackData(DRIVER_INFO.name()).build());

        return new InlineKeyboardMarkup(new ArrayList<>(Arrays.asList(firstRow, secondRow, thirdRow)));
    }

    public static InlineKeyboardMarkup getMainMenuKeyboard() {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();

        firstRow.add(InlineKeyboardButton.builder().text(I_AM_PASSENGER).callbackData(PASSENGER_MENU.name()).build());

        firstRow.add(InlineKeyboardButton.builder().text(I_AM_DRIVER).callbackData(DRIVER_VALIDATION.name()).build());

        return new InlineKeyboardMarkup(of(firstRow));
    }

    public static InlineKeyboardMarkup getAvailableServicesKeyboard() {
        InlineKeyboardButton cancelRegistration = InlineKeyboardButton.builder().text(MAIN_MENU)
                .callbackData(START_GLOBAL_COMMAND).build();

        InlineKeyboardButton startRegistration = InlineKeyboardButton.builder().text(START_REGISTRATION)
                .callbackData(GET_FIRST_NAME.name()).build();

        return new InlineKeyboardMarkup(of(of(cancelRegistration, startRegistration)));
    }

    private static String buildCallbackFromHandlerAndIdParam(HandlerName handlerName, int idParam) {
        return String.join(DEFAULT_SEPARATOR, handlerName.name(), String.valueOf(idParam));
    }

    private static String buildCallbackFromHandlerAndIdParam(HandlerName handlerName, long idParam) {
        return String.join(DEFAULT_SEPARATOR, handlerName.name(), String.valueOf(idParam));
    }

    private static String buildCallbackFromHandlerAndIdParam(HandlerName handlerName, String idParam) {
        return String.join(DEFAULT_SEPARATOR, handlerName.name(), idParam);
    }

    public static InlineKeyboardMarkup getAvailableCountriesKeyboard(Page<Country> countries, HandlerName navigationCallback,
                                                                     HandlerName countryCallback) {
        List<Country> countryList = countries.toList();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (hasPreviousPage(countries)) {
            rows.add(of(InlineKeyboardButton.builder().text(BACK_BUTTON)
                    .callbackData(buildCallbackFromHandlerAndIdParam(navigationCallback, countries.getNumber() - 1)).build()));
        }

        for (int i = 0; i < countryList.size(); i = i + 2) {
            List<InlineKeyboardButton> row = new ArrayList<>(of(getCountryButton(countryList.get(i), countryCallback)));

            if (i + 1 < countryList.size()) {
                row.add(getCountryButton(countryList.get(i + 1), countryCallback));
            }

            rows.add(row);
        }

        if (hasNextPage(countries)) {
            rows.add(of(InlineKeyboardButton.builder().text(NEXT_BUTTON)
                    .callbackData(buildCallbackFromHandlerAndIdParam(navigationCallback, countries.getNumber() + 1)).build()));
        }

        return new InlineKeyboardMarkup(rows);
    }

    private static InlineKeyboardButton getCountryButton(Country country, HandlerName countryCallback) {
        return InlineKeyboardButton.builder().text(country.getName())
                .callbackData(buildCallbackFromHandlerAndIdParam(countryCallback, country.getId())).build();
    }

    public static InlineKeyboardMarkup getAvailableCitiesKeyboard(Page<City> cities, HandlerName navigationCallback,
                                                                  HandlerName cityCallback) {
        List<City> cityList = cities.toList();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (hasPreviousPage(cities)) {
            rows.add(of(InlineKeyboardButton.builder().text(BACK_BUTTON)
                    .callbackData(buildCallbackFromHandlerAndIdParam(navigationCallback, cities.getNumber() - 1)).build()));
        }

        for (int i = 0; i < cityList.size(); i = i + 2) {
            List<InlineKeyboardButton> row = new ArrayList<>(of(getCityButton(cityList.get(i), cityCallback)));

            if (i + 1 < cityList.size()) {
                row.add(getCityButton(cityList.get(i + 1), cityCallback));
            }

            rows.add(row);
        }

        if (hasNextPage(cities)) {
            rows.add(of(InlineKeyboardButton.builder().text(NEXT_BUTTON)
                    .callbackData(buildCallbackFromHandlerAndIdParam(navigationCallback, cities.getNumber() + 1)).build()));
        }

        return new InlineKeyboardMarkup(rows);
    }

    private static InlineKeyboardButton getCityButton(City city, HandlerName cityCallback) {
        return InlineKeyboardButton.builder().text(city.getName())
                .callbackData(buildCallbackFromHandlerAndIdParam(cityCallback, city.getId())).build();
    }

    public static InlineKeyboardMarkup getDriverRoutesMenuKeyboard(Page<Route> routes, HandlerName navigate, HandlerName route) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        rows.add(of(InlineKeyboardButton.builder().text(CREATE_ROUTE).callbackData(ROUTE_CREATION.name()).build()));

        rows.addAll(getDriverRoutesKeyboard(routes, navigate, route).getKeyboard());

        return new InlineKeyboardMarkup(rows);
    }

    public static InlineKeyboardMarkup getDriverRoutesKeyboard(Page<Route> routes, HandlerName navigationCallback,
                                                               HandlerName routeCallback) {
        List<Route> routeList = routes.toList();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (hasPreviousPage(routes)) {
            rows.add(of(InlineKeyboardButton.builder().text(BACK_BUTTON)
                    .callbackData(buildCallbackFromHandlerAndIdParam(navigationCallback, routes.getNumber() - 1)).build()));
        }

        for (Route value : routeList) {
            rows.add(new ArrayList<>(of(getDriverRouteButton(value, routeCallback))));
        }

        if (hasNextPage(routes)) {
            rows.add(of(InlineKeyboardButton.builder().text(NEXT_BUTTON)
                    .callbackData(buildCallbackFromHandlerAndIdParam(navigationCallback, routes.getNumber() + 1)).build()));
        }

        rows.add(of(InlineKeyboardButton.builder().text(BACK_TO_DRIVER_MENU).callbackData(DRIVER_MENU.name()).build()));

        return new InlineKeyboardMarkup(rows);
    }

    private static InlineKeyboardButton getDriverRouteButton(Route route, HandlerName routeCallback) {
        return InlineKeyboardButton.builder().text(format(ROUTE, route.getCityFrom().getName(), route.getCityTo().getName()))
                .callbackData(buildCallbackFromHandlerAndIdParam(routeCallback, route.getId())).build();
    }

    public static InlineKeyboardMarkup getDriverRouteMenuKeyboard(long routeId) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(of(InlineKeyboardButton.builder().text(ALTER_ROUTE_COUNTRY_FROM)
                        .callbackData(buildCallbackFromHandlerAndIdParam(EDIT_ROUTE_COUNTRY_FROM, routeId)).build(),
                InlineKeyboardButton.builder().text(ALTER_ROUTE_CITY_FROM)
                        .callbackData(buildCallbackFromHandlerAndIdParam(EDIT_ROUTE_CITY_FROM, routeId)).build()));

        buttons.add(of(InlineKeyboardButton.builder().text(ALTER_ROUTE_COUNTRY_TO)
                        .callbackData(buildCallbackFromHandlerAndIdParam(EDIT_ROUTE_COUNTRY_TO, routeId)).build(),
                InlineKeyboardButton.builder().text(ALTER_ROUTE_CITY_TO)
                        .callbackData(buildCallbackFromHandlerAndIdParam(EDIT_ROUTE_CITY_TO, routeId)).build()));

        buttons.add(of(InlineKeyboardButton.builder().text(DELETE_ROUTE)
                        .callbackData(buildCallbackFromHandlerAndIdParam(ROUTE_DELETION, routeId)).build(),
                InlineKeyboardButton.builder().text(CREATE_TRIP)
                        .callbackData(buildCallbackFromHandlerAndIdParam(CREATE_TRIP_CHOOSE_ROUTE, routeId)).build()));

        buttons.add(of(InlineKeyboardButton.builder().text(BACK_BUTTON)
                .callbackData(DRIVER_ROUTES.name()).build()));

        return new InlineKeyboardMarkup(buttons);
    }

    public static ReplyKeyboardRemove getRemoveKeyboard() {
        return ReplyKeyboardRemove.builder().removeKeyboard(true).build();
    }

    public static InlineKeyboardMarkup getDriverTripsMenuKeyboard(Page<Trip> trips, HandlerName navigationCallback,
                                                                  HandlerName tripCallback) {
        List<Trip> routeList = trips.toList();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        rows.add(of(InlineKeyboardButton.builder().text(CREATE_TRIP).callbackData(CREATE_DRIVER_TRIP.name()).build()));

        if (hasPreviousPage(trips)) {
            rows.add(of(InlineKeyboardButton.builder().text(BACK_BUTTON)
                    .callbackData(buildCallbackFromHandlerAndIdParam(navigationCallback, trips.getNumber() - 1)).build()));
        }

        for (Trip trip : routeList) {
            rows.add(new ArrayList<>(of(getDriverTripButton(trip, tripCallback))));
        }

        if (hasNextPage(trips)) {
            rows.add(of(InlineKeyboardButton.builder().text(NEXT_BUTTON)
                    .callbackData(buildCallbackFromHandlerAndIdParam(navigationCallback, trips.getNumber() + 1)).build()));
        }

        rows.add(of(InlineKeyboardButton.builder().text(BACK_TO_DRIVER_MENU).callbackData(DRIVER_MENU.name()).build()));

        return new InlineKeyboardMarkup(rows);
    }

    private static InlineKeyboardButton getDriverTripButton(Trip trip, HandlerName tripCallback) {
        return InlineKeyboardButton.builder().text(format(TRIP, trip.getRoute().getCityFrom().getName(),
                        trip.getRoute().getCityTo().getName(), trip.getDepartureDate().format(ofPattern(DATE_FORMAT))))
                .callbackData(buildCallbackFromHandlerAndIdParam(tripCallback, trip.getId())).build();
    }

    public static InlineKeyboardMarkup getContinueCreateDriverTripKeyboard() {
        InlineKeyboardButton startOver = InlineKeyboardButton.builder().text(START_CREATE_TRIP_OVER)
                .callbackData(START_TRIP_OVER_CREATION.name()).build();
        InlineKeyboardButton continueCreate = InlineKeyboardButton.builder().text(CONTINUE_CREATE_TRIP)
                .callbackData(CONTINUE_TRIP_CREATION.name()).build();
        return new InlineKeyboardMarkup(of(of(startOver), of(continueCreate)));
    }

    public static InlineKeyboardMarkup getDriverTripMenuKeyboard(long tripId) {
        InlineKeyboardButton editTripButton = InlineKeyboardButton.builder().text(EDIT_TRIP)
                .callbackData(buildCallbackFromHandlerAndIdParam(DRIVER_TRIP_DETAILS, tripId)).build();
        InlineKeyboardButton deleteTripButton = InlineKeyboardButton.builder().text(DELETE_TRIP)
                .callbackData(buildCallbackFromHandlerAndIdParam(DRIVER_TRIP_DELETION, tripId)).build();
        InlineKeyboardButton backToDriverTripsButton = InlineKeyboardButton.builder().text(BACK_TO_DRIVER_TRIPS)
                .callbackData(DRIVER_TRIPS.name()).build();
        return new InlineKeyboardMarkup(of(of(editTripButton), of(deleteTripButton), of(backToDriverTripsButton)));
    }

    public static InlineKeyboardMarkup getDriverTripDetailsKeyboard(long tripId, HandlerName navigationCallback) {
        InlineKeyboardButton departureDate = InlineKeyboardButton.builder().text(EDIT_TRIP_DEPARTURE_DATE)
                .callbackData(buildCallbackFromHandlerAndIdParam(DRIVER_TRIP_EDITING_DEPARTURE_DATE, tripId)).build();
        InlineKeyboardButton departureTime = InlineKeyboardButton.builder().text(EDIT_TRIP_DEPARTURE_TIME)
                .callbackData(buildCallbackFromHandlerAndIdParam(DRIVER_TRIP_EDITING_DEPARTURE_TIME, tripId)).build();
        InlineKeyboardButton arrivalDate = InlineKeyboardButton.builder().text(EDIT_TRIP_ARRIVAL_DATE)
                .callbackData(buildCallbackFromHandlerAndIdParam(DRIVER_TRIP_EDITING_ARRIVAL_DATE, tripId)).build();
        InlineKeyboardButton arrivalTime = InlineKeyboardButton.builder().text(EDIT_TRIP_ARRIVAL_TIME)
                .callbackData(buildCallbackFromHandlerAndIdParam(DRIVER_TRIP_EDITING_ARRIVAL_TIME, tripId)).build();
        InlineKeyboardButton pickupPoint = InlineKeyboardButton.builder().text(EDIT_TRIP_PICKUP_POINT)
                .callbackData(buildCallbackFromHandlerAndIdParam(DRIVER_TRIP_EDITING_PICKUP_POINT, tripId)).build();
        InlineKeyboardButton dropOffPoint = InlineKeyboardButton.builder().text(EDIT_TRIP_DROPOFF_POINT)
                .callbackData(buildCallbackFromHandlerAndIdParam(DRIVER_TRIP_EDITING_DROPOFF_POINT, tripId)).build();
        InlineKeyboardButton currency = InlineKeyboardButton.builder().text(EDIT_CURRENCY)
                .callbackData(buildCallbackFromHandlerAndIdParam(DRIVER_TRIP_EDITING_CURRENCY, tripId)).build();
        InlineKeyboardButton price = InlineKeyboardButton.builder().text(EDIT_TRIP_PRICE)
                .callbackData(buildCallbackFromHandlerAndIdParam(DRIVER_TRIP_EDITING_PRICE, tripId)).build();
        InlineKeyboardButton baggageInfo = InlineKeyboardButton.builder().text(EDIT_TRIP_BAGGAGE_INFO)
                .callbackData(buildCallbackFromHandlerAndIdParam(DRIVER_TRIP_EDITING_BAGGAGE_INFO, tripId)).build();
        InlineKeyboardButton otherInfo = InlineKeyboardButton.builder().text(EDIT_TRIP_OTHER_INFO)
                .callbackData(buildCallbackFromHandlerAndIdParam(DRIVER_TRIP_EDITING_OTHER_INFO, tripId)).build();
        InlineKeyboardButton deleteTrip = InlineKeyboardButton.builder().text(DELETE_TRIP)
                .callbackData(buildCallbackFromHandlerAndIdParam(DRIVER_TRIP_DELETION, tripId)).build();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (!navigationCallback.equals(DRIVER_TRIP_DETAILS_LESS)) {
            rows.add(of(currency, price));
            rows.add(of(baggageInfo, otherInfo));
            rows.add(of(deleteTrip));
        } else {
            rows.add(of(departureDate, departureTime));
            rows.add(of(arrivalDate, arrivalTime));
            rows.add(of(pickupPoint, dropOffPoint));
        }

        rows.add(getNavigationButtons(DRIVER_TRIPS, navigationCallback, tripId));

        return new InlineKeyboardMarkup(rows);
    }

    private static List<InlineKeyboardButton> getNavigationButtons(HandlerName backCallback, HandlerName moreCallback, long id) {
        InlineKeyboardButton backButton = InlineKeyboardButton.builder().callbackData(backCallback.name())
                .text(BACK_TO_MENU).build();
        InlineKeyboardButton previousButton = InlineKeyboardButton.builder().text(MORE_OPTIONS)
                .callbackData(buildCallbackFromHandlerAndIdParam(moreCallback, id)).build();
        return of(backButton, previousButton);
    }

    public static InlineKeyboardMarkup getCurrenciesKeyboard() {
        InlineKeyboardButton euro = InlineKeyboardButton.builder().text(EURO)
                .callbackData(buildCallbackFromHandlerAndIdParam(CREATE_TRIP_CHOOSE_CURRENCY, EUR.name())).build();
        InlineKeyboardButton uah = InlineKeyboardButton.builder().text(UAH)
                .callbackData(buildCallbackFromHandlerAndIdParam(CREATE_TRIP_CHOOSE_CURRENCY, UA.name())).build();
        return new InlineKeyboardMarkup(of(of(euro, uah)));
    }

    public static InlineKeyboardMarkup getPassengerMainMenuKeyboard() {
        List<InlineKeyboardButton> first =
                of(InlineKeyboardButton.builder().text(FIND_TRIP).callbackData(FINDING_TRIP.name()).build());
        List<InlineKeyboardButton> second =
                of(InlineKeyboardButton.builder().text(TRACKING_ROUTES_MENU).callbackData(TRACKING_ROUTES.name()).build());
        return new InlineKeyboardMarkup(of(first, second));
    }

    public static InlineKeyboardMarkup getNoTripsKeyboard(long routeId) {
        List<InlineKeyboardButton> firstRow = of(InlineKeyboardButton.builder().text(START_TRACK_TRIP)
                .callbackData(joinHandlerAndParam(TRACK_TRIP, routeId)).build());
        List<InlineKeyboardButton> secondRow = of(InlineKeyboardButton.builder()
                .text(START_LOOKING_FOR_TRIP).callbackData(FINDING_TRIP.name()).build());
        List<InlineKeyboardButton> thirdRow = of(InlineKeyboardButton.builder()
                .text(BACK_TO_PASSENGER_MENU).callbackData(PASSENGER_MENU.name()).build());
        return new InlineKeyboardMarkup(of(firstRow, secondRow, thirdRow));
    }

    public static InlineKeyboardMarkup getAvailableTripsForPassengerKeyboard(Page<Trip> trips, HandlerName navigationCallback,
                                                                             HandlerName tripCallback) {
        List<Trip> tripsList = trips.toList();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (hasPreviousPage(trips)) {
            rows.add(of(InlineKeyboardButton.builder().text(BACK_BUTTON)
                    .callbackData(buildCallbackFromHandlerAndIdParam(navigationCallback, trips.getNumber() - 1)).build()));
        }

        for (Trip value : tripsList) {
            rows.add(new ArrayList<>(of(getDriverTripButton(value, tripCallback))));
        }

        if (hasNextPage(trips)) {
            rows.add(of(InlineKeyboardButton.builder().text(NEXT_BUTTON)
                    .callbackData(buildCallbackFromHandlerAndIdParam(navigationCallback, trips.getNumber() + 1)).build()));
        }

        rows.add(of(InlineKeyboardButton.builder().text(FIND_TRIP).callbackData(FINDING_TRIP.name()).build()));

        return new InlineKeyboardMarkup(rows);
    }

    public static InlineKeyboardMarkup getAvailablePassengerTrackingRoutesKeyboard(Page<Route> countries,
                                                                                   HandlerName navigationCallback,
                                                                                   HandlerName countryCallback) {
        List<Route> countryList = countries.toList();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (hasPreviousPage(countries)) {
            rows.add(of(InlineKeyboardButton.builder().text(BACK_BUTTON)
                    .callbackData(buildCallbackFromHandlerAndIdParam(navigationCallback, countries.getNumber() - 1)).build()));
        }

        for (int i = 0; i < countryList.size(); i = i + 2) {
            rows.add(of(getPassengerRouteButton(countryList.get(i), countryCallback)));

            if (i + 1 < countryList.size()) {
                rows.add(of(getPassengerRouteButton(countryList.get(i + 1), countryCallback)));
            }
        }

        List<InlineKeyboardButton> lowerRow = new ArrayList<>(of(InlineKeyboardButton.builder().text(PASSENGER_MAIN_MENU)
                .callbackData(PASSENGER_MENU.name()).build()));

        if (hasNextPage(countries)) {
            lowerRow.add(InlineKeyboardButton.builder().text(NEXT_BUTTON)
                    .callbackData(buildCallbackFromHandlerAndIdParam(navigationCallback, countries.getNumber() + 1)).build());
        }

        rows.add(lowerRow);

        return new InlineKeyboardMarkup(rows);
    }

    private static boolean hasPreviousPage(Page<?> page) {
        return !page.isFirst() && page.getTotalPages() > 1;
    }

    private static boolean hasNextPage(Page<?> page) {
        return !page.isLast() && page.getTotalPages() > 1;
    }

    private static InlineKeyboardButton getPassengerRouteButton(Route route, HandlerName countryCallback) {
        return InlineKeyboardButton.builder().text(route.getSimplifiedRoute())
                .callbackData(buildCallbackFromHandlerAndIdParam(countryCallback, route.getId())).build();
    }

    public static InlineKeyboardMarkup getPassengerTripDetailsKeyboard(Trip trip) {
        return InlineKeyboardMarkup.builder().keyboardRow(of(
                InlineKeyboardButton.builder().text(GET_TRIP_DETAILS)
                        .callbackData(buildCallbackFromHandlerAndIdParam(TRACKING_ROUTE_TRIP_DETAILS, trip.getId()))
                        .build())).build();
    }
    public static InlineKeyboardMarkup getPassengerChosenTripDetailsKeyboard(long cityId, long tripId, HandlerName navigationCallback){
        InlineKeyboardButton purchaseTicket = InlineKeyboardButton.builder().text(PURCHASE_TICKET)
                .callbackData(buildCallbackFromHandlerAndIdParam(PURCHASING_TICKETS, tripId)).build();
        InlineKeyboardButton backToPassengerMenu = InlineKeyboardButton.builder().text(BACK_TO_PASSENGER_MENU)
                .callbackData(PASSENGER_MENU.name()).build();
        InlineKeyboardButton back = InlineKeyboardButton.builder().text(BACK_BUTTON)
                .callbackData(buildCallbackFromHandlerAndIdParam(navigationCallback,cityId)).build();
        return new InlineKeyboardMarkup(of(of(purchaseTicket),of(backToPassengerMenu,back)));
    }
}
