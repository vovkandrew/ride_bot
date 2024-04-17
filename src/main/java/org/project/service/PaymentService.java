package org.project.service;

import org.project.model.CreatePaymentLinkResponse;
import org.project.model.PaymentStatusRequest;
import org.project.model.Trip;
import org.project.util.enums.Currency;

import java.util.Optional;

public interface PaymentService {
    Optional<CreatePaymentLinkResponse> generatePaymentLink(Trip trip, long amount, long bookingId);

    void processBookingPayment(long bookingId, PaymentStatusRequest request);
}
