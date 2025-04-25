package org.exp.xo3bot.services.multigame;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
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

    private final MultiGameUserService multiGameUserService;

    private final TelegramBot telegramBot;
    private final BotButtons buttons;
    private final UserService userService;


    public MultiGame gamePlayersInfoFiller(MultiGame multiGame, Long userId, CallbackQuery callbackQuery) {
        //Optional<MultiGameUser> optionalMultiGameUser = multiGameUserRepository.findById(userId);

        MultiGameUser player = multiGameUserService.getOrCreatePlayer(callbackQuery);

        if (multiGame.getPlayerX() == null) {

            if (!multiGame.getPlayerO().getId().equals(userId)){
                multiGame.setPlayerX(player);

                telegramBot.execute(
                        new EditMessageText(multiGame.getInlineMessageId(),
                                "❌" + multiGame.getPlayerX().getFullname()
                                        +
                                        "\n⭕" + multiGame.getPlayerO().getFullname()
                        ).replyMarkup(buttons.getBoardBtns(multiGame.getId(), multiGame.getGameBoard()))
                );

                saveGame(multiGame);

            }/* else {
                telegramBot.execute(
                        new AnswerCallbackQuery(callbackQuery.id()).text("You played with X").showAlert(true)
                );
                //return null;
            }*/

        } else if (multiGame.getPlayerO() == null) {

            if (!multiGame.getPlayerX().getId().equals(userId)){
                multiGame.setPlayerO(player);

                telegramBot.execute(
                        new EditMessageText(multiGame.getInlineMessageId(),
                                "❌" + multiGame.getPlayerX().getFullname()
                                        +
                                        "\n⭕" + multiGame.getPlayerO().getFullname()
                        ).replyMarkup(buttons.getBoardBtns(multiGame.getId(), multiGame.getGameBoard()))
                );

                saveGame(multiGame);

            } /*else {
                telegramBot.execute(
                        new AnswerCallbackQuery(callbackQuery.id()).text("You played with X").showAlert(true)
                );
                //return null;
            }*/
        }

        return multiGameRepository.save(multiGame);
    }


    public MultiGame getOrCreateMultiGame() {
        System.out.println("MultiGameService.getOrCreateMultiGame");
        MultiGame multiGame = null;

        Optional<MultiGame> multiGameByStatus = multiGameRepository.findFirstByStatus();
        System.out.println("multiGameByStatus = " + multiGameByStatus);

        if (multiGameByStatus.isPresent()) {
            multiGame = multiGameByStatus.get();

            System.out.println("multiGame = " + multiGame);

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
            System.out.println("multiGame = " + multiGame);
        }

        System.out.println("multiGame = " + multiGame);
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