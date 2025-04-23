package org.exp.xo3bot.services.multigame;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.entity.MultiGame;
import org.exp.xo3bot.entity.stats.GameStatus;
import org.exp.xo3bot.repos.MultiGameRepository;
import org.exp.xo3bot.services.base.BotButtons;
import org.exp.xo3bot.services.base.GameBoardService;
import org.exp.xo3bot.services.msg.ResourceMessageManager;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MultiGameLogic {

    //private final MainDto dto;
    private final GameBoardService gameBoardService;
    private final MultiGameRepository multiGameRepository;
    private final TelegramBot telegramBot;
    private final BotButtons buttons;
    private final ResourceMessageManager rm;

    public void handleMove(Long gameId, String data, Long userId, CallbackQuery callbackQuery) {

        String[] parts = data.split("_");
        int row = Integer.parseInt(parts[2]);
        int col = Integer.parseInt(parts[3]);

        Optional<MultiGame> optionalGame = multiGameRepository.findById(gameId);

        if (optionalGame.isEmpty()) return;

        MultiGame game = optionalGame.get();

        String inlineMessageId = game.getInlineMessageId();

        if (inlineMessageId == null) {
            System.err.println("❌ inlineMessageId is NULL for gameId = " + gameId);
            return;
        }

        // Faqat faol o‘yin bo‘lsa
        if (!game.getStatus().equals(GameStatus.ACTIVE)) {
            telegramBot.execute(new AnswerCallbackQuery(callbackQuery.id()).text("Game is not active!").showAlert(true));
            return;
        }

        // Faqat navbatdagi o‘yinchi yurishi mumkin
        if (game.getPlayerO() != null){
            if (!game.getCurrentTurnId().equals(userId)) {
                telegramBot.execute(new AnswerCallbackQuery(callbackQuery.id()).text("Not your turn!").showAlert(true));
                return;
            }
        }

        int[][] board = game.getGameBoard();

        if (board[row][col] != 0) {
            telegramBot.execute(new AnswerCallbackQuery(callbackQuery.id()).text("Invalid move!").showAlert(true));
            return;
        }

        int mark = game.getPlayerX().getId().equals(userId) ? 1 : 2;
        board[row][col] = mark;

        game.setGameBoard(board);
        game.setUpdatedAt(LocalDateTime.now());

        if (gameBoardService.checkWin(board, mark)) {
            game.setStatus(GameStatus.FINISHED);

            String winnerName = mark == 1 ? game.getPlayerX().getFullname() : game.getPlayerO().getFullname();

            EditMessageText winMessage = new EditMessageText(
                    game.getInlineMessageId(),
                    "🏆 " + winnerName + " won the game!"
            ).replyMarkup(buttons.endMultiGameBtns());

            telegramBot.execute(winMessage);

        } else if (gameBoardService.checkDraw(board)) {
            game.setStatus(GameStatus.FINISHED);

            EditMessageText drawMessage = new EditMessageText(
                    game.getInlineMessageId(),
                    "🤝 It's a draw!"
            ).replyMarkup(buttons.endMultiGameBtns());

            telegramBot.execute(drawMessage);

        } else {

            if (game.getPlayerO() != null) {

                Long nextTurnId = game.getPlayerX().getId().equals(userId)
                        ? game.getPlayerO().getId()
                        : game.getPlayerX().getId();
                game.setCurrentTurnId(nextTurnId);

            } else {
                game.setCurrentTurnId(null);
            }

            InlineKeyboardMarkup markup = buttons.getBoardBtns(gameId, board);
            EditMessageReplyMarkup replyMarkup = new EditMessageReplyMarkup(
                    game.getInlineMessageId()
            ).replyMarkup(markup);
            telegramBot.execute(replyMarkup);
        }

        multiGameRepository.save(game);
    }
}
