package org.project.service.impl;

import org.project.TelegramWebhookBot;
import org.project.model.*;
import org.project.service.BookingService;
import org.project.service.NotificationService;
import org.project.service.PaymentService;
import org.project.util.enums.Currency;
import org.project.util.enums.Status;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.URI;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.project.util.Keyboards.getDriverTripMenuKeyboard;
import static org.project.util.constants.Messages.*;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;
import static org.telegram.telegrambots.meta.api.methods.ParseMode.HTML;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final String destinationComment = "Бронювання поїздки за маршрутом %s на %s";
    private final String xToken = System.getenv("monobankXToken");
    private final RestTemplate restTemplate = new RestTemplate();
    private final BookingService bookingService;
    private final NotificationService notificationService;

    public PaymentServiceImpl(BookingService bookingService, NotificationService notificationService) {
        this.bookingService = bookingService;
        this.notificationService = notificationService;
    }

    @Override
    public Optional<CreatePaymentLinkResponse> generatePaymentLink(Trip trip, long amount, long bookingId) {
        URI uri = fromUriString("https://api.monobank.ua").path("/api/merchant/invoice/create").build().toUri();

        String formattedDestinationComment = String.format(destinationComment, trip.getRoute().getSimplifiedRoute(),
                trip.getFormattedDepartureDate());

        MerchantPaymInfoRequest merchantPaymInfoRequest = MerchantPaymInfoRequest.builder()
                .reference(String.valueOf(bookingId))
                .destination(formattedDestinationComment)
                .comment(formattedDestinationComment)
                .build();

        CreatePaymentLinkRequest request = CreatePaymentLinkRequest.builder().amount(amount)
                .ccy(trip.getCurrency().getCcy())
                .merchantPaymInfo(merchantPaymInfoRequest)
                .webHookUrl(createWebhookUrl(bookingId))
                .build();

        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("X-Token", xToken);

        ResponseEntity<CreatePaymentLinkResponse> exchangeResult;

        try {
            exchangeResult = restTemplate.exchange(uri.toString(), POST, new HttpEntity<>(request, headers),
                    CreatePaymentLinkResponse.class);
        } catch (Exception e) {
            System.out.printf("Exception thrown while trying to create payment link for booking %d%n", bookingId);
            System.out.println("\nCause: " + e.getCause());
            System.out.println("\nMessage: " + e.getMessage());

            throw new RuntimeException(e);
        }

        if (exchangeResult.getStatusCode().is2xxSuccessful()) {
            return ofNullable(exchangeResult.getBody());
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public void processBookingPayment(long bookingId, PaymentStatusRequest request) {
        Booking booking = bookingService.getBooking(bookingId);

        if (ofNullable(request.getStatus()).isPresent()) {
            if (request.getStatus().equals("success")) {
                booking.setStatus(Status.CREATED);
                booking = bookingService.update(booking);

                notificationService.notifyPassengerAboutSuccessfulPayment(booking);

                notificationService.notifyDriverAboutBookedSeatsForHisTrip(booking);
            } else if (request.getStatus().equals("expired")) {
                //may require
                bookingService.deleteBooking(booking);

                notificationService.notifyPassengerAboutExpiredPaymentLink(booking);
            }
        }
    }

    private String createWebhookUrl(long bookingId) {
        return System.getenv("serverAddress") + "/booking/" + bookingId + "/payment/status";
    }
}
