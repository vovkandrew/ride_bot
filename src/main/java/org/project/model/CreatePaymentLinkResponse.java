package org.project.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentLinkResponse {
    private String invoiceId;
    private String pageUrl;
}
