package org.exp.xo3bot;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.exp.xo3bot.utils.Constants.BOT_TOKEN;

@SpringBootApplication
public class Xo3botApplication {

    public static void main(String[] args) {
        SpringApplication.run(Xo3botApplication.class, args);
    }

    @Configuration
    public class BotConfig {
        @Bean
        public TelegramBot telegramBot() {
            return new TelegramBot(BOT_TOKEN);
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
