package org.project.config;

import org.project.TelegramWebhookBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

import java.util.Objects;

@Configuration
public class SpringConfig {
    private final Environment environment;

    public SpringConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(Objects.requireNonNull(environment.getProperty("bot.webhook-url"))).build();
    }

    @Bean
    public TelegramWebhookBot springWebhookBot(SetWebhook setWebhook) {
        TelegramWebhookBot bot = new TelegramWebhookBot(setWebhook);

        bot.setBotPath(environment.getProperty("bot.webhook-url"));
        bot.setBotUsername(environment.getProperty("bot.username"));
        bot.setBotToken(environment.getProperty("bot.token"));

        return bot;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(environment.getProperty("spring.datasource.url"));
        driverManagerDataSource.setUsername(environment.getProperty("spring.datasource.username"));
        driverManagerDataSource.setPassword(environment.getProperty("spring.datasource.password"));
        driverManagerDataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        return new JdbcTemplate(driverManagerDataSource);
    }
}
