package org.exp.xo3bot.processes.callbackquery.botgame;


import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entity.User;
import org.exp.xo3bot.entity.stats.Difficulty;
import org.exp.xo3bot.processes.CabinetCmd;

@RequiredArgsConstructor
public class DifficultyChangerMenuCmd implements Runnable{
    private final User user;
    private final String data;
    private final MainDto dto;

    @Override
    public void run() {
        String[] parts = data.split("_");
        if (parts.length > 1) {
            String difficulty = parts[1].toUpperCase();
            user.getGame().setDifficulty(Difficulty.valueOf(difficulty));

            user.getGame().setMessageId(
                    dto.getTelegramBot().execute(
                            new SendMessage(user.getId(), "changed to: " + difficulty)
                    ).message().messageId());
        }

        dto.getUserRepository().save(user);

        new CabinetCmd(user, dto).run();
    }
}
