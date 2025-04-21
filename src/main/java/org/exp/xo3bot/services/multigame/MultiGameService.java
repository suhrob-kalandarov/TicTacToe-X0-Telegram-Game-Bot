package org.exp.xo3bot.services.multigame;

import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.entities.MultiGame;
import org.exp.xo3bot.entities.stats.GameStatus;
import org.exp.xo3bot.repos.MultiGameRepository;
import org.exp.xo3bot.services.user.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MultiGameService {

    private final MultiGameRepository multiGameRepository;
    private final UserService userService;

    public Long getOrCreateMultiGame(Long creatorId) {

        Optional<MultiGame> multiGameByStatus = multiGameRepository.findFirstByStatus();

        if (multiGameByStatus.isPresent()) {

            MultiGame multiGame = multiGameByStatus.get();
            multiGame.setCreatorId(creatorId);
            multiGame.setStatus(GameStatus.ACTIVE);
            multiGame.setGameBoard(new int[3][3]);

            return multiGameRepository.save(multiGame).getId();
        }

        MultiGame newMultiGame = MultiGame.builder()
                .creatorId(creatorId)
                .status(GameStatus.CREATED)
                .gameBoard(new int[3][3])
                .build();
        return multiGameRepository.save(newMultiGame).getId();
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
}