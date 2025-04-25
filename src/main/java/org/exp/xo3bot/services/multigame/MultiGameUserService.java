package org.exp.xo3bot.services.multigame;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.ChosenInlineResult;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.entity.multigame.MultiGameUser;
import org.exp.xo3bot.repos.MultiGameUserRepository;
import org.exp.xo3bot.services.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MultiGameUserService {

    private final UserService userService;
    private final MultiGameUserRepository multiGameUserRepository;

    public MultiGameUser getOrCreatePlayer(CallbackQuery callbackQuery) {
        MultiGameUser multiGameUser = null;
        Long userId = callbackQuery.from().id();
        Optional<MultiGameUser> optionalMultiGameUser = multiGameUserRepository.findById(userId);

        if (optionalMultiGameUser.isEmpty()) {
            multiGameUser = MultiGameUser.builder()
                    .id(userId)
                    .fullname(userService.buildFullNameFromUpdate(callbackQuery))
                    .username(callbackQuery.from().username())
                    .languageCode(callbackQuery.from().languageCode())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            MultiGameUser savedMultiGameUser = multiGameUserRepository.save(multiGameUser);
            System.out.println("savedMultiGameUser = " + savedMultiGameUser);
            return savedMultiGameUser;

        } else {
            multiGameUser = optionalMultiGameUser.get();
            System.out.println("multiGameUser = " + multiGameUser);
            return multiGameUser;
        }
    }

    public MultiGameUser getOrCreatePlayer(ChosenInlineResult chosenInlineResult) {
        MultiGameUser multiGameUser = null;
        Long userId = chosenInlineResult.from().id();

        Optional<MultiGameUser> optionalMultiGameUser = multiGameUserRepository.findById(userId);
        System.out.println("optionalMultiGameUser findById(" + userId + ") = " + optionalMultiGameUser);

        if (optionalMultiGameUser.isEmpty()) {
            multiGameUser = MultiGameUser.builder()
                    .id(userId)
                    .fullname(userService.buildFullNameFromUpdate(chosenInlineResult))
                    .username(chosenInlineResult.from().username())
                    .languageCode(chosenInlineResult.from().languageCode())
                    .build();
            System.out.println("Build multiGameUser = " + multiGameUser);

            System.out.println("multiGameUser = " + multiGameUser);
            MultiGameUser savedMultiGameUser = multiGameUserRepository.save(multiGameUser);
            System.out.println("savedMultiGameUser = " + savedMultiGameUser);

            return savedMultiGameUser;

        } else {
            multiGameUser = optionalMultiGameUser.get();
            System.out.println("Get multiGameUserFromDB = " + multiGameUser);
            return multiGameUser;
        }
    }
}


/*

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

        dto.getMultiGameRepository().save(multiGame);

        if (data.startsWith("MOVE_")) {
        dto.getMultiGameLogic().handleMove(gameId,data, userId, callbackQuery);
        }
    }
}
 */