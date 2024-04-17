package org.project.controller;

import org.project.model.PaymentStatusRequest;
import org.project.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PaymentStatusController {
    private final PaymentService paymentService;

    public PaymentStatusController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/booking/{booking-id}/payment/status")
    @ResponseStatus(HttpStatus.OK)
    public void updateBookingPaymentStatus(@PathVariable(name = "booking-id") long bookingId,
                                           @RequestBody PaymentStatusRequest request) {
        paymentService.processBookingPayment(bookingId, request);
    }
}
