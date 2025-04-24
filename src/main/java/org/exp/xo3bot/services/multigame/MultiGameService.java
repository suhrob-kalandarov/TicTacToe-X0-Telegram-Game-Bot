package org.exp.xo3bot.services.multigame;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.EditMessageText;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entity.multigame.MultiGame;
import org.exp.xo3bot.entity.multigame.MultiGameUser;
import org.exp.xo3bot.entity.stats.GameStatus;
import org.exp.xo3bot.repos.MultiGameRepository;
import org.exp.xo3bot.repos.MultiGameUserRepository;
import org.exp.xo3bot.services.base.BotButtons;
import org.exp.xo3bot.services.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MultiGameService {


    private final MultiGameUserRepository multiGameUserRepository;
    private final MultiGameRepository multiGameRepository;

    private final TelegramBot telegramBot;
    private final BotButtons buttons;
    private final UserService userService;


    public void gamePlayersInfoFiller(MultiGame multiGame, Long userId, CallbackQuery callbackQuery) {
        Optional<MultiGameUser> optionalMultiGameUser = multiGameUserRepository.findById(userId);

        if (multiGame.getPlayerX() == null && !multiGame.getPlayerO().getId().equals(userId)) {
            if (optionalMultiGameUser.isEmpty()) {
                MultiGameUser playerX = MultiGameUser.builder()
                        .id(userId)
                        .fullname(userService.buildFullNameFromUpdate(callbackQuery))
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
                        .fullname(userService.buildFullNameFromUpdate(callbackQuery))
                        .username(callbackQuery.from().username())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .languageCode(callbackQuery.from().languageCode())
                        .build();
                multiGame.setPlayerO(playerO);

            } else {
                multiGame.setPlayerO(optionalMultiGameUser.get());
            }

            telegramBot.execute(
                    new EditMessageText(multiGame.getInlineMessageId(),
                            "❌" + multiGame.getPlayerX().getFullname()
                                    +
                                    "\n⭕" + multiGame.getPlayerO().getFullname()
                    ).replyMarkup(buttons.getBoardBtns(multiGame.getId(), multiGame.getGameBoard()))
            );
        }
    }


    public MultiGame getOrCreateMultiGame() {
        MultiGame multiGame;
        Optional<MultiGame> multiGameByStatus = multiGameRepository.findFirstByStatus();

        if (multiGameByStatus.isPresent()) {
            multiGame = multiGameByStatus.get();

            multiGame.setCreatorId(null);
            multiGame.setStatus(GameStatus.ACTIVE);

            multiGame.setInlineMessageId(null);

            multiGame.setPlayerO(null);
            multiGame.setPlayerX(null);
            multiGame.setInTurn(null);

            multiGame.setUpdatedAt(LocalDateTime.now());

            multiGame.setGameBoard(new int[3][3]);
            //multiGame.initGameBoard();

        } else {
            multiGame = MultiGame.builder()
                    .status(GameStatus.CREATED)
                    .gameBoard(new int[3][3])
                    .build();
        }
        return multiGame;
    }

    public Optional<MultiGame> getGame(long gameId) {
        return multiGameRepository.findById(gameId);
    }

    public void saveGame(MultiGame game) {
        multiGameRepository.save(game);
    }

    public void endGame(long gameId) {
        Optional<MultiGame> game = getGame(gameId);
        if (game.isPresent()) {
            saveGame(game.orElse(null));
        }
    }

    public void saveInlineMessageId(Long gameId, String inlineMessageId) {
        Optional<MultiGame> optionalGame = multiGameRepository.findById(gameId);
        if (optionalGame.isPresent()) {
            MultiGame game = optionalGame.get();
            game.setInlineMessageId(inlineMessageId);
            multiGameRepository.save(game);
        }
    }

    /* in future
    @Scheduled(fixedRate = 60000)
    public void markInactiveGamesAsDead() {
        List<MultiGame> activeGames = multiGameRepository.findAll().stream()
                .filter(g -> g.getStatus() == GameStatus.FINISHED || g.getStatus() == GameStatus.IDLE)
                .collect(Collectors.toList());

        LocalDateTime now = LocalDateTime.now();

        for (MultiGame game : activeGames) {
            Duration duration = Duration.between(game.getUpdatedAt(), now);
            if (duration.toMinutes() >= 5) {
                game.setStatus(GameStatus.FINISHED);

                multiGameRepository.save(game);

                EditMessageText expiredMessage = new EditMessageText(
                        game.getInlineMessageId(),
                        "⌛ Game expired due to inactivity."
                ).replyMarkup(null);

                telegramBot.execute(expiredMessage);
            }
        }
    }*/
}