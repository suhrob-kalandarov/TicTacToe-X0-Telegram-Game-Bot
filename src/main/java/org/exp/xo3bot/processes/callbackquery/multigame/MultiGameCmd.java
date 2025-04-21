package org.exp.xo3bot.processes.callbackquery.multigame;

import com.pengrad.telegrambot.model.CallbackQuery;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entities.User;


@RequiredArgsConstructor
public class MultiGameCmd implements Runnable {

    private final MainDto dto;

    private final User user;
    private final CallbackQuery callbackQuery;

    @Override
    public void run() {
        String data = callbackQuery.data();
        String[] parts = data.split("_");

        if (parts.length < 3) return;

        Long gameId = Long.parseLong(parts[2]);

        if (data.startsWith("SELECT_X_")) {
            dto.getMultiGameLogic().joinGame(user, gameId, "X", callbackQuery);

        } else if (data.startsWith("SELECT_O_")) {
            dto.getMultiGameLogic().joinGame(user, gameId, "O", callbackQuery);

        } else if (data.startsWith("MOVE_")) {
            int row = Integer.parseInt(parts[3]);
            int col = Integer.parseInt(parts[4]);

            dto.getMultiGameLogic().handleMove(gameId, row, col, user.getId(), callbackQuery);
        }
    }
}
