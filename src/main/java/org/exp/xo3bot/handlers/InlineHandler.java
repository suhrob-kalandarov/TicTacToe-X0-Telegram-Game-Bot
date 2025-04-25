package org.exp.xo3bot.handlers;

import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entity.multigame.MultiGame;
import org.exp.xo3bot.usekeys.Handler;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class InlineHandler implements Handler<InlineQuery> {

    private final MainDto dto;

    @Override
    public void handle(InlineQuery inlineQuery) {
        System.out.println("InlineHandler.handle" + inlineQuery.toString());
        try {
            Long creatorId = inlineQuery.from().id();
            String fullName = dto.getUserService().buildFullNameFromUpdate(inlineQuery);

            MultiGame multiGame = dto.getMultiGameService().getOrCreateMultiGame();
            multiGame.setCreatorId(creatorId);
            System.out.println("multiGame = " + multiGame);

            MultiGame savedMultiGame = dto.getMultiGameRepository().save(multiGame);
            System.out.println("saved multigame = " + savedMultiGame);

            InlineQueryResult[] results = new InlineQueryResult[]{
                    new InlineQueryResultArticle("selected_x_" + multiGame.getId(), "❌ START GAME ⭕", "x")
                            .inputMessageContent(new InputTextMessageContent("❌ " + fullName + "\n⭕ - ?"))
                            .replyMarkup(dto.getButtons().getBoardBtns(multiGame.getId(), new int[3][3]))
            };

            System.out.println("inline query results = " + Arrays.toString(results));

            BaseResponse baseResponse = dto.getTelegramBot().execute(new AnswerInlineQuery(inlineQuery.id(), results)
                    .button(
                            new InlineQueryResultsButton("@xoDemoBot", "bot_uri")
                    )
            );

            System.out.println("baseResponse.isOk() = " + baseResponse.isOk());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


/*
new InlineQueryResultArticle("selected_x_" + multiGame.getId(), "❌", "x")
                            .inputMessageContent(new InputTextMessageContent("❌ " + fullName + "\n⭕ - ?"))
                            .replyMarkup(dto.getButtons().getBoardBtns(multiGame.getId(), new int[3][3]))

                    , new InlineQueryResultArticle("selected_o_" + multiGame.getId(), "⭕", "o")
                            .inputMessageContent(new InputTextMessageContent("\n❌ - ? \n ⭕ " + fullName))
                            .replyMarkup(dto.getButtons().getBoardBtns(multiGame.getId(), new int[3][3])),
*/