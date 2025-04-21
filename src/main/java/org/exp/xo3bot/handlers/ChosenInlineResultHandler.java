package org.exp.xo3bot.handlers;


import com.pengrad.telegrambot.model.ChosenInlineResult;
import com.pengrad.telegrambot.model.User;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entities.MultiGame;
import org.exp.xo3bot.entities.stats.GameStatus;
import org.exp.xo3bot.usekeys.Handler;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ChosenInlineResultHandler implements Handler<ChosenInlineResult> {

    private final MainDto dto;

    @Override
    public void handle(ChosenInlineResult chosenInlineResult) {
        System.out.println(chosenInlineResult);

        User tgUser = chosenInlineResult.from();
        String resultId = chosenInlineResult.resultId();
        Long gameId = null;

        Pattern pattern = Pattern.compile("selected_[xo]_(\\d+)");
        Matcher matcher = pattern.matcher(resultId);

        if (matcher.matches()) {
            gameId = Long.parseLong(matcher.group(1));
        }

        if (resultId.startsWith("selected_x")) {

            Optional<MultiGame> optionalMultiGame = dto.getMultiGameRepository().findById(Objects.requireNonNull(gameId));

            if (optionalMultiGame.isPresent()) {

                MultiGame multiGame = optionalMultiGame.get();

                multiGame.setPlayerX(
                        org.exp.xo3bot.entities.User.builder()
                                .id(tgUser.id())
                                .languageCode(tgUser.languageCode())
                                .fullname(dto.getUserService().buildFullNameFromUpdate(chosenInlineResult))
                                .build()
                );
                multiGame.setCurrentTurnId(tgUser.id());

                System.out.println(dto.getMultiGameRepository().save(multiGame).getPlayerX());

                if (multiGame.getStatus().equals(GameStatus.ACTIVE)) {



                } else {

                }
            }

        } else if (resultId.startsWith("selected_o")) {

            Optional<MultiGame> optionalMultiGame = dto.getMultiGameRepository().findById(gameId);

            if (optionalMultiGame.isPresent()) {

                MultiGame multiGame = optionalMultiGame.get();

                if (multiGame.getStatus().equals(GameStatus.ACTIVE)) {

                    multiGame.setPlayerO(
                            org.exp.xo3bot.entities.User.builder()
                                    .id(tgUser.id())
                                    .languageCode(tgUser.languageCode())
                                    .fullname(dto.getUserService().buildFullNameFromUpdate(chosenInlineResult))
                                    .build()
                    );

                    System.out.println(dto.getMultiGameRepository().save(multiGame).getPlayerO());

                } else {

                }
            }
        }
    }
}
