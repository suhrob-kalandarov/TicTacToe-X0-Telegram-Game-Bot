package org.exp.xo3bot.utils;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.ChosenInlineResult;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;

import org.exp.xo3bot.Xo3botApplication;
import org.exp.xo3bot.handlers.ChosenInlineResultHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import org.exp.xo3bot.handlers.CallbackHandler;
import org.exp.xo3bot.handlers.InlineHandler;
import org.exp.xo3bot.handlers.MessageHandler;

import static org.exp.xo3bot.utils.Log.log;

@RequiredArgsConstructor
@Component
public class BotRunner implements CommandLineRunner {

    private final TelegramBot bot;
    private final Xo3botApplication.ExecutorConfig executorService;

    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;
    private final InlineHandler inlineHandler;
    private final ChosenInlineResultHandler chosenInlineResultHandler;

    @Override
    public void run(String... args) {
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                executorService.executorService().execute(() -> {
                    if (update.message() != null) {
                        messageHandler.handle(update.message());

                    } else if (update.callbackQuery() != null) {
                        callbackHandler.handle(update.callbackQuery());

                    } else if (update.inlineQuery() != null) {
                        inlineHandler.handle(update.inlineQuery());

                    } else if (update.chosenInlineResult() != null) {
                        chosenInlineResultHandler.handle(update.chosenInlineResult());

                    } else log("Unknown message - " + update);
                });
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
