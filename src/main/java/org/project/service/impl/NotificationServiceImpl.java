package org.project.service.impl;

import lombok.AllArgsConstructor;
import org.project.TelegramWebhookBot;
import org.project.model.Route;
import org.project.model.Trip;
import org.project.service.NotificationService;
import org.project.service.RouteService;
import org.project.util.Keyboards;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.String.format;
import static org.project.util.constants.Messages.NEW_TRACKING_TRIP_CREATED;
import static org.telegram.telegrambots.meta.api.methods.ParseMode.HTML;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final RouteService routeService;
    private final TelegramWebhookBot webhookBot;

    @Override
    public void notifyAboutNewTrip(Trip trip) {
        Route route = trip.getRoute();

        //forEach instead of stream due to significance of having accessible forEach variables
        for (long telegramUserId: routeService.getAllUsersWhoTrackRoute(route)) {
            try {
                webhookBot.executeAsync(SendMessage.builder().chatId(telegramUserId).parseMode(HTML)
                        .text(format(NEW_TRACKING_TRIP_CREATED, route.getSimplifiedRoute()))
                        .replyMarkup(Keyboards.getPassengerTripDetailsKeyboard(trip)).build());
            } catch (TelegramApiException e) {
                System.out.printf("Failed to notify user %d about newly created trip %d", telegramUserId, trip.getId());
                System.out.println("\nCause: " + e.getCause());
                System.out.println("Message: " + e.getMessage());

                throw new RuntimeException(e);
            }
        }
    }
}
