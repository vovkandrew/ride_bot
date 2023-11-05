package org.project.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetWebhookModel {
    private boolean ok = false;
    private boolean result = false;
    private String description = "";
}
