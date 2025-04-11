package org.exp.xo3bot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.entities.User;
import org.exp.xo3bot.faces.Handler;
import org.exp.xo3bot.faces.Process;
import org.exp.xo3bot.processes.CabinetCmd;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageHandler implements Handler<Message> {

    private final TelegramBot telegramBot;

    @Override
    public void handle(Message message) {
        Process process = null;
        String text = message.text();

        if (text.equals("/start")) {
            //process = new CabinetCmd(telegramBot, new User());
        }

        process.run();
    }
}
