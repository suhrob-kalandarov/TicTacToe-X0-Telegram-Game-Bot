package org.exp.xo3bot.processes;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.entities.User;
import org.exp.xo3bot.faces.Process;

@RequiredArgsConstructor
public class CabinetCmd implements Process {

    private final TelegramBot telegramBot;
    private final User user;

    @Override
    public void run() {

        telegramBot.execute(new SendMessage(user.getId(), "Welcome to bot!"));
    }
}
