package org.project.model;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantPaymInfoRequest {
    private String reference;
    private String destination;
    private String comment;
    @Builder.Default
    private List<String> customerEmails = new ArrayList<>();
    @Builder.Default
    private List<Object> basketOrder = new ArrayList<>();
}
