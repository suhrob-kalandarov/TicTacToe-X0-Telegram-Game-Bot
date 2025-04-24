package org.exp.xo3bot.processes.callbackquery.multigame;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.EditMessageText;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entity.multigame.MultiGame;
import org.exp.xo3bot.entity.multigame.MultiGameUser;
import org.exp.xo3bot.entity.stats.GameStatus;

import java.time.LocalDateTime;
import java.util.Optional;


@RequiredArgsConstructor
public class MultiGameCmd implements Runnable {

    private final MainDto dto;
    private final CallbackQuery callbackQuery;

    @Override
    public void run() {
        String data = callbackQuery.data();
        Long userId = callbackQuery.from().id();

        String[] parts = data.split("_");
        Long gameId = Long.parseLong(parts[1]);

        Optional<MultiGame> optionalMultiGame = dto.getMultiGameRepository().findById(gameId);

        if (optionalMultiGame.isEmpty()) {
            dto.getTelegramBot().execute(
                    new AnswerCallbackQuery(callbackQuery.id()).text("Game not exist!").showAlert(true)
            );
            return;
        }

        MultiGame multiGame = optionalMultiGame.get();

        if (multiGame.getPlayerX() == null || multiGame.getPlayerO() == null) {
            dto.getMultiGameService().gamePlayersInfoFiller(multiGame, userId, callbackQuery);
        }

       /* if (multiGame.getPlayerX() != null || multiGame.getPlayerO() != null) {
            multiGame.setStatus(GameStatus.ACTIVE);
        }*/

        dto.getMultiGameRepository().save(multiGame);

        if (data.startsWith("MOVE_")) {
            dto.getMultiGameLogic().handleMove(gameId,data, userId, callbackQuery);
        }
    }
}