package org.exp.xo3bot.processes.callbackquery.multigame;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entity.MultiGame;
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
        String languageCode = callbackQuery.from().languageCode();

        String[] parts = data.split("_");
        Long gameId = Long.parseLong(parts[1]);

        Optional<MultiGame> optionalMultiGame = dto.getMultiGameRepository().findById(gameId);

        if (optionalMultiGame.isEmpty()) {
            System.out.println("Not exist!");
            return;
        }

        MultiGame multiGame = optionalMultiGame.get();
        Optional<MultiGameUser> optionalMultiGameUser = dto.getMultiGameUserRepository().findById(userId);

        if (multiGame.getPlayerX() == null && !multiGame.getPlayerO().getId().equals(userId)) {
            if (optionalMultiGameUser.isEmpty()) {
                MultiGameUser playerX = MultiGameUser.builder()
                        .id(callbackQuery.from().id())
                        .fullname(dto.getUserService().buildFullNameFromUpdate(callbackQuery))
                        .username(callbackQuery.from().username())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                multiGame.setPlayerX(playerX);

            } else {
                multiGame.setPlayerO(optionalMultiGameUser.get());
            }

        } else if (multiGame.getPlayerO() == null && !multiGame.getPlayerX().getId().equals(userId)) {
            if (optionalMultiGameUser.isEmpty()) {
                MultiGameUser playerO = MultiGameUser.builder()
                        .id(userId)
                        .fullname(dto.getUserService().buildFullNameFromUpdate(callbackQuery))
                        .username(callbackQuery.from().username())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .languageCode(languageCode)
                        .build();
                multiGame.setPlayerO(playerO);

            } else {
                multiGame.setPlayerO(optionalMultiGameUser.get());
            }

            dto.getTelegramBot().execute(
                    new EditMessageText(multiGame.getInlineMessageId(),
                            "❌" + multiGame.getPlayerX().getFullname()
                                    +
                                    "\n⭕" + multiGame.getPlayerO().getFullname()
                    )
            );
        }

        if (multiGame.getPlayerX() != null || multiGame.getPlayerO() != null) {
            multiGame.setStatus(GameStatus.ACTIVE);
        }

        dto.getMultiGameRepository().save(multiGame);

        if (data.startsWith("MOVE_")) {
            dto.getMultiGameLogic().handleMove(gameId,data, userId, callbackQuery);
        }
    }
}