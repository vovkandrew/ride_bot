package org.project.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentLinkRequest {
    //in cents
    private long amount;
    private long ccy;
    private MerchantPaymInfoRequest merchantPaymInfo;
    private String webHookUrl;
    //in seconds, equal to 15 minutes
    private final long validity = 900;
    private final String paymentType = "debit";
}
