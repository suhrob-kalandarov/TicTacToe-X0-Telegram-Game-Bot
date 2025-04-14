package org.exp.xo3bot;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.exp.xo3bot.repos")
public class Xo3botApplication {

    public static void main(String[] args) {
        SpringApplication.run(Xo3botApplication.class, args);
    }

    @Value("${telegram.bot.token}")
    private String botToken;

    @Configuration
    public class BotConfig {
        @Value("${telegram.bot.token}")
        private String botToken;

        @Bean
        public TelegramBot telegramBot() {
            return new TelegramBot(botToken);
        }
    }

    @Configuration
    public class ExecutorConfig {
        @Bean
        public ExecutorService executorService() {
            return Executors.newFixedThreadPool(10);
        }
    }
}
