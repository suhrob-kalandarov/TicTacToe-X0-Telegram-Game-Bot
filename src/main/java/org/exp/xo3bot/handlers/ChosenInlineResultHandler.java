package org.exp.xo3bot.handlers;

import com.pengrad.telegrambot.model.ChosenInlineResult;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entity.multigame.MultiGame;
import org.exp.xo3bot.entity.multigame.MultiGameUser;
import org.exp.xo3bot.entity.multigame.Turn;
import org.exp.xo3bot.usekeys.Handler;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ChosenInlineResultHandler implements Handler<ChosenInlineResult> {

    private final MainDto dto;

    @Override
    public void handle(ChosenInlineResult chosenInlineResult) {

        String inlinedMessageId = chosenInlineResult.inlineMessageId();
        String resultId = chosenInlineResult.resultId();

        Long userId = chosenInlineResult.from().id();

        Pattern pattern = Pattern.compile("selected_[xo]_(\\d+)");
        Matcher matcher = pattern.matcher(resultId);

        if (!matcher.matches()) return;

        Long gameId = Long.parseLong(matcher.group(1));
        Optional<MultiGame> optionalMultiGame = dto.getMultiGameRepository().findById(gameId);

        if (optionalMultiGame.isEmpty()) return;

        MultiGame multiGame = optionalMultiGame.get();
        multiGame.setInlineMessageId(inlinedMessageId);

        MultiGameUser multiGameUser = null;
        Optional<MultiGameUser> optionalMultiGameUser = dto.getMultiGameUserRepository().findById(userId);

        if (optionalMultiGameUser.isEmpty()) {
            multiGameUser = MultiGameUser.builder()
                    .id(userId)
                    .fullname(dto.getUserService().buildFullNameFromUpdate(chosenInlineResult))
                    .username(chosenInlineResult.from().username())
                    .languageCode(chosenInlineResult.from().languageCode())
                    .build();
            dto.getMultiGameUserRepository().save(multiGameUser);

        } else {
            multiGameUser = optionalMultiGameUser.get();
        }

        if (resultId.startsWith("selected_x")) {

            // Agar Player O allaqachon shu odam bo'lsa, ruxsat bermaslik
            if (multiGame.getPlayerO() != null && userId.equals(multiGame.getPlayerO().getId())) {

                dto.getTelegramBot().execute(
                        new AnswerCallbackQuery(inlinedMessageId).text("User already joined as O !")
                );

                System.out.println("User already joined as O");
                return;
            }
            multiGame.setPlayerX(multiGameUser);
            multiGame.setInTurn(Turn.PLAYER_X);

        } else if (resultId.startsWith("selected_o")) {

            // Agar Player X allaqachon shu odam bo'lsa, ruxsat bermaslik
            if (multiGame.getPlayerX() != null && userId.equals(multiGame.getPlayerX().getId())) {

                dto.getTelegramBot().execute(
                        new AnswerCallbackQuery(inlinedMessageId).text("User already joined as X !")
                );

                System.out.println("User already joined as X");
                return;
            }
            multiGame.setPlayerO(multiGameUser);
            multiGame.setInTurn(Turn.PLAYER_X);
        }

        dto.getMultiGameRepository().save(multiGame);
    }
}