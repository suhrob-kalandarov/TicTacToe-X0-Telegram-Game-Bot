package org.exp.xo3bot.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.entities.MultiGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InlineService {

    @Autowired
    private final TelegramBot telegramBot;

    public void handle(InlineQuery inlineQuery) {
        try {
            Long creatorId = inlineQuery.from().id();
            Long gameId = createNewGame(creatorId);

            InlineKeyboardMarkup joinMarkup = new InlineKeyboardMarkup(
                    new InlineKeyboardButton("Join as ❌").callbackData("SELECT_X_" + gameId),
                    new InlineKeyboardButton("Join as ⭕").callbackData("SELECT_O_" + gameId)
            );

            InlineQueryResult[] results = new InlineQueryResult[]{
                    new InlineQueryResultArticle("join_game", "Start Game", "Iltimos, o'yinga qo'shiling!")
                            .inputMessageContent(new InputTextMessageContent("Opponent is waiting..."))
                            .replyMarkup(joinMarkup)
            };

            telegramBot.execute(new AnswerInlineQuery(inlineQuery.id(), results));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Long createNewGame(Long creatorId) {

        Long gameId = (long) -1;

        try {
            MultiGame game = new MultiGame();
            game.setCreatorId(creatorId);
            game.initializeBoard();

            entityManager.persist(game);
            entityManager.flush();
            transaction.commit();

            gameId = game.getId();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return gameId;
    }
}
