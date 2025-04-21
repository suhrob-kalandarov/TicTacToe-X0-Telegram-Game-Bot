package org.exp.xo3bot.processes.callbackquery.botgame;

import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entities.User;
import org.exp.xo3bot.utils.Constants;

@RequiredArgsConstructor
public class DifficultyMenuCmd implements Runnable {
    private final User user;
    private final MainDto dto;

    @Override
    public void run() {
        SendResponse response = (SendResponse) dto.getTelegramBot().execute(
                new EditMessageText(
                        user.getId(), user.getGame().getMessageId(),
                        dto.getRm().getString(Constants.CHOOSE_DIFFICULTY_LEVEL)
                                /*.formatted(
                                        dto.getRm().getString(Constants.LEVEL + user.getGame().getDifficulty())
                                )*/
                ).replyMarkup(
                        dto.getButtons().genDifficultyLevelButtons()
                )
        );
        user.getGame().setMessageId(response.message().messageId());

        dto.getUserRepository().save(user);
    }
}
