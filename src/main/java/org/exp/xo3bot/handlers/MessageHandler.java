package org.exp.xo3bot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.entities.User;
import org.exp.xo3bot.usekeys.Handler;
import org.exp.xo3bot.processes.CabinetCmd;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class MessageHandler implements Handler<Message> {

    private final TelegramBot telegramBot;

    @Override
    public void handle(Message message) {
        Runnable process = null;
        String text = message.text();

        if (text.equals("/start")) {
            process = new CabinetCmd(telegramBot, User.builder().id(message.from().id()).build());

        } else if (text.equals("/")) {

        }


        Objects.requireNonNull(process).run();
    }
}
