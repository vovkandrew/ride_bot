package org.project.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusRequest {
    private String invoiceId;
    private String status;
    private String failureReason;
    private String errCode;
    private long amount;
    private long ccy;
    private long finalAmount;
    private String createdDate;
    private String modifiedDate;
    private String reference;
}
