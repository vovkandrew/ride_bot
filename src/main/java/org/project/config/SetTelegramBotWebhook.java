package org.project.config;

import lombok.AllArgsConstructor;
import org.project.model.SetWebhookModel;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Component
@AllArgsConstructor
public class SetTelegramBotWebhook implements ApplicationListener<ContextRefreshedEvent> {
    private final Environment environment;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String setWebhookPath = "/bot{bot-token}/setWebhook";
    private final Pattern descriptionPattern = compile("Webhook (is already set|was set)");

    //this method binds app url with telegram to receive updates via webhook instead doing it manually on each app run
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            URI uri = fromUriString("https://api.telegram.org/").path(setWebhookPath)
                    .queryParam("url", System.getenv("serverAddress"))
                    .buildAndExpand(System.getenv("telegramBotToken")).toUri();

            ResponseEntity<SetWebhookModel> exchangeResult = restTemplate
                    .exchange(uri.toString(), GET, null, SetWebhookModel.class);

            if (exchangeResult.getStatusCode().is2xxSuccessful()) {
                SetWebhookModel model = exchangeResult.getBody();

                if (Optional.ofNullable(model).isPresent() && (!model.isOk() || !model.isResult()
                        || !descriptionPattern.matcher(model.getDescription()).matches())) {
                    System.out.printf("Failed to set webhook. Response code %d. Response body %s",
                            exchangeResult.getStatusCode().value(), model);

                    System.exit(exchangeResult.getStatusCode().value());
                }
            }
        } catch (Exception e) {
            System.out.println("Exception thrown while trying setting bot webhook");
            System.out.println("\nCause: " + e.getCause());
            System.out.println("\nMessage: " + e.getMessage());

            throw new RuntimeException(e);
        }
    }
}
