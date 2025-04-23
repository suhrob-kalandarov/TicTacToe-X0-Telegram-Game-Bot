package org.exp.xo3bot.handlers;

import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entity.MultiGame;
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
            String fullName = dto.getUserService().buildFullNameFromUpdate(inlineQuery);
            MultiGame multiGame = dto.getMultiGameService().getOrCreateMultiGame();
            multiGame.setCurrentTurnId(creatorId);
            dto.getMultiGameRepository().save(multiGame);

            InlineQueryResult[] results = new InlineQueryResult[]{
                    new InlineQueryResultArticle("selected_x_" + multiGame.getId(), "❌", "!")
                            .inputMessageContent(new InputTextMessageContent("❌ " + fullName + "\n⭕ - ?"))
                            .replyMarkup(dto.getButtons().getBoardBtns(multiGame.getId(), new int[3][3])),

                    new InlineQueryResultArticle("selected_o_" + multiGame.getId(), "⭕", "!")
                            .inputMessageContent(new InputTextMessageContent("\n❌ - ? \n ⭕ " + fullName))
                            .replyMarkup(dto.getButtons().getBoardBtns(multiGame.getId(), new int[3][3])),
            };

            dto.getTelegramBot().execute(new AnswerInlineQuery(inlineQuery.id(), results)
                    .button(
                            new InlineQueryResultsButton("@xoDemoBot", "bot_starter_uri")
                    )
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
