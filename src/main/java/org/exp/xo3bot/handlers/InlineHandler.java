package org.exp.xo3bot.handlers;

import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.usekeys.Handler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InlineHandler implements Handler<InlineQuery> {

    private final MainDto dto;

    @Override
    public void handle(InlineQuery inlineQuery) {
        try {
            Long creatorId = inlineQuery.from().id();

            // MultiGameLogic orqali o'yin yaratish
            Long gameId = dto.getMultiGameService().getOrCreateMultiGame(creatorId);

            InlineKeyboardMarkup joinMarkup = new InlineKeyboardMarkup(
                    new InlineKeyboardButton("Join as ❌").callbackData("SELECT_X_" + gameId),
                    new InlineKeyboardButton("Join as ⭕").callbackData("SELECT_O_" + gameId)
            );


            InlineQueryResult[] results = new InlineQueryResult[]{
                    new InlineQueryResultArticle("selected_x_" + gameId, "❌", "Please join the game!")
                            .inputMessageContent(new InputTextMessageContent("Opponent is waiting..."))
                            .replyMarkup(dto.getButtons().getBoardBtns(gameId, new int[3][3])),

                    new InlineQueryResultArticle("selected_o_" + gameId, "⭕", "Please join the game!")
                            .inputMessageContent(new InputTextMessageContent("Opponent is waiting..."))
                            .replyMarkup(dto.getButtons().getBoardBtns(gameId, new int[3][3])),
            };


            dto.getTelegramBot().execute(new AnswerInlineQuery(inlineQuery.id(), results)
                    .button(
                            new InlineQueryResultsButton("@xoDemoBot", "bot_uri")
                    )
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
