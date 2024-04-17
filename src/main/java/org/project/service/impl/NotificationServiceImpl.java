package org.project.service.impl;

import lombok.AllArgsConstructor;
import org.project.TelegramWebhookBot;
import org.project.model.Booking;
import org.project.model.Route;
import org.project.model.Trip;
import org.project.service.NotificationService;
import org.project.service.RouteService;
import org.project.service.UserMessageService;
import org.project.util.Keyboards;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.lang.String.format;
import static org.project.util.Keyboards.*;
import static org.project.util.constants.Messages.*;
import static org.telegram.telegrambots.meta.api.methods.ParseMode.HTML;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final RouteService routeService;
    private final TelegramWebhookBot webhookBot;
    private final UserMessageService userMessageService;

    @Override
    public void notifyPassengersAboutNewCreatedTrip(Trip trip) {
        Route route = trip.getRoute();

        //forEach instead of stream due to significance of having accessible forEach variables
        for (long telegramUserId: routeService.getAllUsersWhoTrackRoute(route)) {
            try {
                webhookBot.executeAsync(SendMessage.builder().chatId(telegramUserId).parseMode(HTML)
                        .text(format(NEW_TRACKING_TRIP_CREATED, route.getSimplifiedRoute()))
                        .replyMarkup(getPassengerTripDetailsKeyboard(trip)).build());
            } catch (TelegramApiException e) {
                System.out.printf("Failed to notify user %d about newly created trip %d", telegramUserId, trip.getId());
                System.out.println("\nCause: " + e.getCause());
                System.out.println("Message: " + e.getMessage());

                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void notifyPassengerAboutSuccessfulPayment(Booking booking) {
        Trip trip = booking.getTrip();

        try {
            String message = format(BOOKING_PAYMENT_RECEIVED, booking.getNumberOfBookedSeats(),
                    trip.getRoute().getSimplifiedRoute(),
                    trip.getFormattedDepartureDate());

            //TODO:add functionality to provide booking details
            webhookBot.executeAsync(SendMessage.builder().chatId(booking.getTelegramUser().getTelegramId())
                    .parseMode(HTML).text(message).replyMarkup(getPassengerMainMenuKeyboard()).build());
        } catch (TelegramApiException e) {
            System.out.printf("Failed to notify passenger %d about receiving payment for booking %d",
                    booking.getTelegramUser().getTelegramId(), booking.getId());
            System.out.println("\nCause: " + e.getCause());
            System.out.println("Message: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyDriverAboutBookedSeatsForHisTrip(Booking booking) {
        Trip trip = booking.getTrip();

        try {

            String message = format(TRIP_SEATS_BOOKED, trip.getRoute().getSimplifiedRoute(),
                    trip.getFormattedDepartureDate(), booking.getNumberOfBookedSeats());

            webhookBot.executeAsync(SendMessage.builder().chatId(trip.getRoute().getTelegramUserId())
                    .parseMode(HTML).text(message).replyMarkup(getDriverTripMenuKeyboard(trip.getId())).build());
        } catch (TelegramApiException e) {
            System.out.printf("Failed to notify driver %d about receiving payment for booking %d",
                    booking.getTelegramUser().getTelegramId(), booking.getId());
            System.out.println("\nCause: " + e.getCause());
            System.out.println("Message: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyPassengerAboutExpiredPaymentLink(Booking booking) {
        try {
            Trip trip = booking.getTrip();

            String message = format(PAYMENT_LINK_EXPIRED, trip.getRoute().getSimplifiedRoute(),
                    trip.getFormattedDepartureDate());

            CompletableFuture<Message> sentMessage = webhookBot.executeAsync(SendMessage.builder()
                    .chatId(trip.getRoute().getTelegramUserId()).parseMode(HTML).text(message)
                    .replyMarkup(getPassengerTripDetailsKeyboard(trip)).build());

            userMessageService.createRemovableMessage(trip.getRoute().getTelegramUserId(),
                    sentMessage.get().getMessageId());

        } catch (Exception e) {
            System.out.printf("Failed to notify passenger %d about expired payment for booking %d",
                    booking.getTelegramUser().getTelegramId(), booking.getId());
            System.out.println("\nCause: " + e.getCause());
            System.out.println("Message: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }
}
