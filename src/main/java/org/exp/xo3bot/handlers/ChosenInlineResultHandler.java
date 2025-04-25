package org.exp.xo3bot.handlers;

import com.pengrad.telegrambot.model.ChosenInlineResult;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entity.multigame.MultiGame;
import org.exp.xo3bot.entity.multigame.MultiGameUser;
import org.exp.xo3bot.entity.multigame.Turn;
import org.exp.xo3bot.services.multigame.MultiGameUserService;
import org.exp.xo3bot.usekeys.Handler;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ChosenInlineResultHandler implements Handler<ChosenInlineResult> {

    private final MainDto dto;
    private final MultiGameUserService multiGameUserService;

    @Override
    public void handle(ChosenInlineResult chosenInlineResult) {

        System.out.println("ChosenInlineResultHandler.handle" + chosenInlineResult.toString());

        String inlinedMessageId = chosenInlineResult.inlineMessageId();
        String resultId = chosenInlineResult.resultId();
        Long userId = chosenInlineResult.from().id();

        Pattern pattern = Pattern.compile("selected_[xo]_(\\d+)");
        System.out.println("pattern = " + pattern);

        Matcher matcher = pattern.matcher(resultId);
        System.out.println("matcher = " + matcher);

        if (!matcher.matches()) {
            System.out.println("Not matches :!: " + matcher);
            return;
        }

        Long gameId = Long.parseLong(matcher.group(1));
        System.out.println("gameId = " + gameId);

        Optional<MultiGame> optionalMultiGame = dto.getMultiGameRepository().findById(gameId);
        System.out.println("optionalMultiGame = " + optionalMultiGame);

        if (optionalMultiGame.isEmpty()) {
            System.out.println("Not found optionalMultiGame = " + optionalMultiGame);
            return;
        }

        MultiGame multiGame = optionalMultiGame.get();
        System.out.println("multiGame = " + multiGame);

        multiGame.setInlineMessageId(inlinedMessageId);
        System.out.println("inlinedMessageId = " + inlinedMessageId);
        System.out.println("multiGame.getInlineMessageId() = " + multiGame.getInlineMessageId());

        MultiGameUser multiGameUser = multiGameUserService.getOrCreatePlayer(chosenInlineResult);

        if (resultId.startsWith("selected_x")) {
            System.out.println("resultId = " + resultId);

            /// Agar Player O allaqachon shu odam bo'lsa, ruxsat bermaslik
            if (multiGame.getPlayerO() != null && userId.equals(multiGame.getPlayerO().getId())) {

                dto.getTelegramBot().execute(
                        new AnswerCallbackQuery(inlinedMessageId).text("User(" + userId + ") already joined as O !")
                );

                System.out.println("User(" + userId + ") already joined as O");
                return;
            }


            System.out.println("multiGame.getPlayerX() = " + multiGame.getPlayerX());
            multiGame.setPlayerX(multiGameUser);
            System.out.println("multiGame.getPlayerX() = " + multiGame.getPlayerX());

            System.out.println("multiGame.getInTurn() = " + multiGame.getInTurn());
            multiGame.setInTurn(Turn.PLAYER_X);
            System.out.println("multiGame.getInTurn() = " + multiGame.getInTurn());

        }
        System.out.println("multiGame = " + multiGame);
        MultiGame savedMultiGame = dto.getMultiGameRepository().save(multiGame);
        System.out.println("savedMultiGame = " + savedMultiGame);
    }
}


/* in a future version of bot
     else if (resultId.startsWith("selected_o")) {

            /// Agar Player X shu odam bo'lsa, ruxsat bermaslik
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
*/