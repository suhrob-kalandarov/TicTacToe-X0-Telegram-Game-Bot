package org.exp.xo3bot.services.multigame;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.entity.multigame.MultiGame;
import org.exp.xo3bot.entity.multigame.Turn;
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

        if (optionalGame.isEmpty()) {
            telegramBot.execute(
                    new AnswerCallbackQuery(callbackQuery.id()).text("Game not found!").showAlert(true)
            );
            return;
        }

        MultiGame game = optionalGame.get();
        String inlineMessageId = game.getInlineMessageId();

        if (inlineMessageId == null) {
            telegramBot.execute(
                    new AnswerCallbackQuery(callbackQuery.id()).text("❌ inlineMessageId is NULL for gameId = " + gameId).showAlert(true)
            );
            return;
        }

        // Faqat faol o‘yin bo‘lsa
        /*if (game.getStatus() != GameStatus.ACTIVE || game.getStatus() != GameStatus.CREATED) {
            telegramBot.execute(
                    new AnswerCallbackQuery(callbackQuery.id()).text("Game is not active!").showAlert(true)
            );

            telegramBot.execute(
                    new EditMessageText(
                            inlineMessageId, GameStatus.DEAD_LOCK.name()
                    ).replyMarkup(buttons.endMultiGameBtns())
            );

            game.setStatus(GameStatus.DEAD_LOCK);
            multiGameRepository.save(game);
            return;
        }*/


        int[][] board = game.getGameBoard();

        /*if (game.getPlayerX() != null && game.getPlayerO() != null){

            if (game.getPlayerO().getId() != userId || game.getPlayerX().getId() != userId) {
                telegramBot.execute(new AnswerCallbackQuery(callbackQuery.id()).text("It's not your game!").showAlert(true));
                return;
            }

            // Faqat navbatdagi o‘yinchi yurishi mumkin
            if (game.getInTurn() != Turn.PLAYER_O) { // && !game.getCurrentTurnId().equals(userId)
                telegramBot.execute(new AnswerCallbackQuery(callbackQuery.id()).text("Not your turn!").showAlert(true));
                return;
            }

        } else {

        }*/


        // 1. O'yinchi tekshiruvi (gamega tegishliligi)
        if (game.getPlayerX() == null || game.getPlayerO() == null) {
            telegramBot.execute(new AnswerCallbackQuery(callbackQuery.id())
                    .text("Game is not ready yet! Waiting for second player...")
                    .showAlert(true));
            return;
        }

        // 2. O'yinchi bu o'yinda o'ynayotganligini tekshirish
        if (!userId.equals(game.getPlayerX().getId()) && !userId.equals(game.getPlayerO().getId())) {
            telegramBot.execute(new AnswerCallbackQuery(callbackQuery.id())
                    .text("It's not your game!")
                    .showAlert(true));
            return;
        }

        // 3. Navbat tekshiruvi
        if ((game.getInTurn() == Turn.PLAYER_X && !userId.equals(game.getPlayerX().getId())) ||
                game.getPlayerO() != null && (game.getInTurn() == Turn.PLAYER_O && !userId.equals(game.getPlayerO().getId())))
        {

            telegramBot.execute(new AnswerCallbackQuery(callbackQuery.id())
                    .text("Not your turn! Wait for your opponent.")
                    .showAlert(true));
            return;
        }

        if (board[row][col] != 0) {
            telegramBot.execute(new AnswerCallbackQuery(callbackQuery.id()).text("Invalid move!"));
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

                switchTurn(game);

            } else {
                game.setInTurn(Turn.PLAYER_O);
            }

            InlineKeyboardMarkup markup = buttons.getBoardBtns(gameId, board);
            EditMessageReplyMarkup replyMarkup = new EditMessageReplyMarkup(
                    game.getInlineMessageId()
            ).replyMarkup(markup);
            telegramBot.execute(replyMarkup);
        }

        multiGameRepository.save(game);
    }

    public void switchTurn(MultiGame game) {
        if (game.getPlayerO() != null) {
            // Agar ikkala o'yinchi ham mavjud bo'lsa
            game.setInTurn(game.getInTurn() == Turn.PLAYER_X
                    ? Turn.PLAYER_O
                    : Turn.PLAYER_X);
        } else {
            // Agar faqat bitta o'yinchi bo'lsa (masalan, hali ikkinchi o'yinchi qo'shilmagan)
            game.setInTurn(Turn.PLAYER_X); // yoki PLAYER_O - qaysi biri boshlashiga qarab
        }
    }
}
