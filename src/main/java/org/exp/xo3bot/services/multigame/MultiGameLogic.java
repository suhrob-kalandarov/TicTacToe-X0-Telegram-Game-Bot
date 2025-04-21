package org.exp.xo3bot.services.multigame;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import lombok.RequiredArgsConstructor;

import org.exp.xo3bot.entities.MultiGame;
import org.exp.xo3bot.entities.User;
import org.exp.xo3bot.entities.stats.GameStatus;
import org.exp.xo3bot.repos.MultiGameRepository;
import org.exp.xo3bot.services.base.BotButtons;
import org.exp.xo3bot.services.msg.ResourceMessageManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MultiGameLogic {

    private final MultiGameRepository multiGameRepository;
    private final TelegramBot telegramBot;
    private final BotButtons buttons;
    private final ResourceMessageManager rm;

    public Long createGame(Long creatorId, String inlineMessageId) {
        MultiGame game = new MultiGame();
        game.setCreatorId(creatorId);
        game.setStatus(GameStatus.CREATED);
        game.setInlineMessageId(inlineMessageId);
        game.initGameBoard();
        multiGameRepository.save(game);

        EditMessageText message = new EditMessageText(
                inlineMessageId, "game.created"

        ).replyMarkup(
                buttons.playAsXorO(game.getId(), creatorId)
        );
        telegramBot.execute(message);

        //return multiGameRepository.findUserByInlineMessageId(inlineMessageId).getGame().getId();
        return null;
    }

    public void joinGame(User user, Long gameId, String side, CallbackQuery callbackQuery) {
        Optional<MultiGame> optionalGame = multiGameRepository.findById(gameId);
        if (optionalGame.isEmpty()) return;

        MultiGame game = optionalGame.get();

        if (game.getStatus() != GameStatus.CREATED && game.getStatus() != GameStatus.WAITING) {
            telegramBot.execute(
                    new AnswerCallbackQuery(
                            callbackQuery.id()
                    ).text("Game already started!").showAlert(true)
            );
            return;
        }

        // Birinchi tanlagan player
        if (side.equals("X") && game.getPlayerX() == null) {
            //game.setPlayerX(user);
            game.setStatus(GameStatus.WAITING);
        } else if (side.equals("O") && game.getPlayerO() == null) {
            //game.setPlayerO(user);
            game.setStatus(GameStatus.WAITING);
        }

        // Agar ikki o'yinchi to'ldirilgan bo'lsa
        if (game.getPlayerX() != null && game.getPlayerO() != null) {
            //game.setCurrentTurnId(game.getPlayerX().getId());
            game.setStatus(GameStatus.ACTIVE);
        }

        multiGameRepository.save(game);

        // Tugmalarni qayta joylash
        InlineKeyboardMarkup markup;
        if (game.getStatus() == GameStatus.WAITING) {
            //markup = buttons.onlyShowSelected(game.getId(), game.getPlayerX(), game.getPlayerO());
        } else {
            markup = buttons.gameBoard(game.getId(), game.getGameBoard());
        }

        EditMessageText edit = new EditMessageText(
                game.getInlineMessageId(),
                "Game started!"
        );//.replyMarkup(markup);


        telegramBot.execute(edit);
    }

    public void handleMove(Long gameId, int row, int col, Long userId, CallbackQuery callbackQuery) {
        Optional<MultiGame> optionalGame = multiGameRepository.findById(gameId);

        if (optionalGame.isEmpty()) return;

        MultiGame game = optionalGame.get();

        if (!game.getStatus().equals(GameStatus.ACTIVE)) {
            telegramBot.execute(new AnswerCallbackQuery(callbackQuery.id()).text("Game is not active!").showAlert(true));
            return;
        }

        if (!game.getCurrentTurnId().equals(userId)) {
            telegramBot.execute(new AnswerCallbackQuery(callbackQuery.id()).text("Not your turn!").showAlert(true));
            return;
        }

        int[][] board = game.getGameBoard();

        if (board[row][col] != 0) {
            telegramBot.execute(new AnswerCallbackQuery(callbackQuery.id()).text("Invalid move!").showAlert(true));
            return;
        }

        int mark =0;// game.getPlayerX().getId().equals(userId) ? 1 : 2;
        board[row][col] = mark;

        game.setGameBoard(board);
        game.setUpdatedAt(LocalDateTime.now());

        if (checkWin(board, mark)) {
            game.setStatus(GameStatus.FINISHED);

            EditMessageText winMessage = new EditMessageText(
                    game.getInlineMessageId(),
                    "game.won"
            ).replyMarkup(null);

            telegramBot.execute(winMessage);

        } else if (isDraw(board)) {
            game.setStatus(GameStatus.FINISHED);

            EditMessageText drawMessage = new EditMessageText(
                    game.getInlineMessageId(),
                    "It's a draw!"
            ).replyMarkup(null);

            telegramBot.execute(drawMessage);
        } else {
            Long nextTurn = null;//game.getPlayerX().getId().equals(userId) ? game.getPlayerO().getId() : game.getPlayerX().getId();
            game.setCurrentTurnId(nextTurn);

            InlineKeyboardMarkup markup = buttons.gameBoard(gameId, board);
            EditMessageReplyMarkup updated = new EditMessageReplyMarkup(game.getInlineMessageId()).replyMarkup(markup);
            telegramBot.execute(updated);
        }

        multiGameRepository.save(game);
    }

    private boolean checkWin(int[][] board, int mark) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == mark && board[i][1] == mark && board[i][2] == mark) return true;
            if (board[0][i] == mark && board[1][i] == mark && board[2][i] == mark) return true;
        }
        return (board[0][0] == mark && board[1][1] == mark && board[2][2] == mark)
                || (board[0][2] == mark && board[1][1] == mark && board[2][0] == mark);
    }

    private boolean isDraw(int[][] board) {
        for (int[] row : board)
            for (int cell : row)
                if (cell == 0) return false;
        return true;
    }

    @Scheduled(fixedRate = 60000) // Har 1 daqiqada tekshirib turadi
    public void markInactiveGamesAsDead() {
        List<MultiGame> activeGames = multiGameRepository.findAll()
                .stream()
                .filter(g -> g.getStatus() == GameStatus.ACTIVE || g.getStatus() == GameStatus.WAITING)
                .collect(Collectors.toList());

        LocalDateTime now = LocalDateTime.now();

        for (MultiGame game : activeGames) {
            Duration duration = Duration.between(game.getUpdatedAt(), now);
            if (duration.toMinutes() >= 5) {
                game.setStatus(GameStatus.FINISHED); // yoki DEAD holati bo‘lsa, enumga qo‘sh
                multiGameRepository.save(game);

                EditMessageText deadMsg = new EditMessageText(
                        game.getInlineMessageId(),
                        "Game expired due to inactivity."
                ).replyMarkup(null);

                telegramBot.execute(deadMsg);
            }
        }
    }

    public Long getOrCreateMultiGame(Long creatorId) {
        return null;
    }
}
