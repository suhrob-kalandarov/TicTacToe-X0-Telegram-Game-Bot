package org.exp.xo3bot.services.multigame;

import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.entity.MultiGame;
import org.exp.xo3bot.entity.stats.GameStatus;
import org.exp.xo3bot.repos.MultiGameRepository;
import org.exp.xo3bot.services.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MultiGameService {

    private final MultiGameRepository multiGameRepository;
    private final UserService userService;

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
            multiGame.setCurrentTurnId(null);

            multiGame.setUpdatedAt(LocalDateTime.now());

            multiGame.initGameBoard();

            return multiGame;

        } else {
            multiGame = MultiGame.builder()
                    .creatorId(null)
                    .status(GameStatus.CREATED)
                    .gameBoard(new int[3][3])
                    .build();
            return multiGame;
        }
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
            game.setInlineMessageId(inlineMessageId); // Entityda qo‘shilgan bo‘lishi kerak
            multiGameRepository.save(game);
        }
    }

    /*@Scheduled(fixedRate = 60000)
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