/*
package org.exp.xo3bot.services.botgame;

import com.pengrad.telegrambot.request.EditMessageText;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entities.User;
import org.springframework.stereotype.Service;

import static org.exp.xo3bot.utils.Constants.*;
import static org.exp.xo3bot.utils.Constants.DRAW_MSG;

@RequiredArgsConstructor
@Service
public class BotGameService {

    private final MainDto dto;

    private User user;

    public void handleMove(int row, int col) {
        try {
            // Restart the board if it's full
            if (isBoardFull(user.getGame().getGameBoard())) {
                user.getGame().initGameBoard();
                //DB.updateGameBoard(user.getUserId(), user.getGameBoard()); // ✅ Game board reset
            }

            // Return if the selected slot is busy
            if (user.getGame().getGameBoard()[row][col] != 0) {
                return;
            }

            // User walkthrough
            user.getGame().getGameBoard()[row][col] = 1;
            //DB.updateGameBoard(user.getUserId(), user.getGameBoard()); // ✅ User walk update

            if (checkWin(user.getGame().getGameBoard(), 1)) {
                updateGameScore(user.getId(), user.getGame().getDifficulty(), "win_count",
                        getUserGameStat(
                                user.getId(),
                                user.getGame().getDifficulty(),
                                "win_count"
                        ) + 1
                );
                sendResult(YOU_WON_MSG);
                return;
            }

            if (isBoardFull(user.getGame().getGameBoard())) {
                updateGameScore(user.getId(), user.getGame().getDifficulty(), "draw_count",
                        getUserGameStat(
                                user.getId(),
                                user.getGame().getDifficulty(),
                                "draw_count"
                        ) + 1
                );
                sendResult(DRAW_MSG);
                return;
            }

            // Bot walk
            int[] botMove = new GameLogic().findBestMove(user.getGame().getGameBoard(), String.valueOf(user.getGame().getDifficulty()));
            user.getGame().getGameBoard()[botMove[0]][botMove[1]] = 2;
            //DB.updateGameBoard(user.getUserId(), user.getGameBoard()); // ✅ Bot walk update

            if (checkWin(user.getGame().getGameBoard(), 2)) {
                updateGameScore(user.getId(), user.getGame().getDifficulty(), "lose_count",
                        getUserGameStat(
                                user.getId(),
                                user.getGame().getDifficulty(),
                                "lose_count"
                        ) + 1
                );
                sendResult(YOU_LOST_MSG);
                return;
            }

            if (isBoardFull(user.getGame().getGameBoard())) {
                updateGameScore(user.getId(), user.getGame().getDifficulty(), "draw_count",
                        getUserGameStat(
                                user.getId(),
                                user.getGame().getDifficulty(),
                                "draw_count"
                        ) + 1
                );
                sendResult(DRAW_MSG);
                return;
            }

            updateGameBoard(); // Refresh the UI
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Update the board
    private void updateGameBoard() {
        EditMessageText editMessage = new EditMessageText(
                user.getId(), user.getGame().getMessageId(), formatGameStartMessage()
        );
        //editMessage.replyMarkup(genGameBoard(getGameBoard(user.getUserId()), getUserSign(user.getUserId())));
        //Main.telegramBot.execute(editMessage);
    }

    private String formatGameStartMessage() {
        */
/*return getString(GAME_MENU_MSG).formatted(
                //DB.getUserSign(user.getUserId()),
                //DB.getBotSign(user.getUserId())
        );*//*

        return null;
    }

    // Find the best walk for the bot
    public int[] findBestMove(int[][] board) {
        return new GameLogic().findBestMove(board, String.valueOf(user.getGame().getDifficulty()));
    }

    // Check for ingestion
    private boolean checkWin(int[][] board, int player) {
        int size = board.length;

        // Horizontal and vertical
        for (int i = 0; i < size; i++) {
            boolean rowWin = true;
            boolean colWin = true;
            for (int j = 0; j < size; j++) {
                if (board[i][j] != player) rowWin = false;
                if (board[j][i] != player) colWin = false;
            }
            if (rowWin || colWin) return true;
        }

        // Diagonal
        boolean diag1 = true;
        boolean diag2 = true;
        for (int i = 0; i < size; i++) {
            if (board[i][i] != player) diag1 = false;
            if (board[i][size - 1 - i] != player) diag2 = false;
        }
        return diag1 || diag2;
    }

    // Check if the board is completely filled
    private boolean isBoardFull(int[][] board) {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) return false;
            }
        }
        return true;
    }

    // Send result message
    private void sendResult(String resultMessage) {
        */
/*Main.telegramBot.execute(
                getResultMessageText(
                        resultMessage,
                        formatBoard(user.getGame().getGameBoard())
                )
        );*//*


        // Send main menu
        */
/*user.getGame().setMessageId(
                telegramBot.execute(
                        new SendMessage(user.getId(), getString(USER_STATISTICS_MSG)
                                .formatted(DB.getUserScores(user.getId()))
                        ).parseMode(ParseMode.valueOf("HTML")
                        ).replyMarkup(BotButtons.genAfterGameCabinetButtons())
                ).message().messageId()
        );*//*




        //updateMessageId(user.getUserId(), user.getMessageId());
        //updateUserState(user.getUserId(), user.getUserState().toString());

        // Initialize game board
        user.getGame().initGameBoard();
        //DB.updateGameBoard(user.getUserId(), user.getGameBoard());
    }

    */
/*@NotNull
    private EditMessageText getResultMessageText(String resultMessage, String boardState) {
        return new EditMessageText(
                user.getId(), user.getGame().getMessageId(),
                RESULT_MSG.formatted(
                        getString(resultMessage),
                        DifficultyLevel.getTrueLevelMsg(user.getDifficultyLevel()),
                        getString(BOARD_MSG)
                ) + boardState
        ).parseMode(
                ParseMode.valueOf("HTML")
        );
    }
    *//*



    //Get game board results
    private String formatBoard(int[][] board) {
        StringBuilder sb = new StringBuilder();
        String padding = "  ";

        sb.append("<pre>");
        */
/*for (int[] row : board) {
            sb.append(padding);
            for (int cell : row) {
                String symbol;
                if (cell == 1) {
                    symbol = getUserSign(user.getUserId());
                } else if (cell == 2) {
                    symbol = getBotSign(user.getUserId());
                } else {
                    symbol = EMPTY_SIGN; // Bo'sh joy
                }
                sb.append(symbol);
                sb.append(padding);
            }
            sb.append("\n");
        }*//*

        sb.append("</pre>");
        return sb.toString();
    }
}
*/
