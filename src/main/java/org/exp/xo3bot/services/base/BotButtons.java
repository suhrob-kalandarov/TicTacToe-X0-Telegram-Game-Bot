package org.exp.xo3bot.services.base;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.exp.xo3bot.entity.User;
import org.exp.xo3bot.entity.stats.Difficulty;
import org.exp.xo3bot.services.msg.ResourceMessageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.exp.xo3bot.utils.Constants.*;

@Service
public class BotButtons {

    @Autowired
    private ResourceMessageManager rm;


    public InlineKeyboardMarkup genCabinetButtons() {
        return new InlineKeyboardMarkup()
                .addRow(new InlineKeyboardButton(rm.getString(PLAY_WITH_BOT_BTN))
                                .callbackData(PLAY_WITH_BOT_BTN),
                        new InlineKeyboardButton(rm.getString(PLAY_WITH_FRIEND_BTN))
                                //.callbackData(PLAY_WITH_FRIEND_BTN)
                                .switchInlineQuery(" ")
                )
                .addRow(new InlineKeyboardButton(rm.getString(DIFFICULTY_LEVEL_BTN))
                        .callbackData(DIFFICULTY_LEVEL_BTN)
                )
                .addRow(new InlineKeyboardButton(rm.getString(STATISTICS_BTN))
                        .callbackData(STATISTICS_BTN)
                )
                .addRow(new InlineKeyboardButton(rm.getString(LANGUAGE_MSG))
                        .callbackData(LANGUAGE_MSG)
                );
    }

    public InlineKeyboardMarkup genAfterGameCabinetButtons() {
        return new InlineKeyboardMarkup()
                .addRow(
                        new InlineKeyboardButton(rm.getString(PLAY_WITH_BOT_BTN))
                                .callbackData(PLAY_WITH_BOT_BTN),
                        new InlineKeyboardButton(rm.getString(PLAY_WITH_FRIEND_BTN))
                                //.callbackData(PLAY_WITH_FRIEND_BTN)
                                //.switchInlineQuery(" ")

                )
                .addRow(new InlineKeyboardButton(rm.getString(DIFFICULTY_LEVEL_BTN))
                        .callbackData(DIFFICULTY_LEVEL_BTN)
                )
                .addRow(new InlineKeyboardButton(rm.getString(SUPPORT_BTN))
                        .callbackData(SUPPORT_BTN)
                )
                .addRow(new InlineKeyboardButton(rm.getString(LANGUAGE_MSG))
                        .callbackData(LANGUAGE_MSG)
                );
    }

    public InlineKeyboardMarkup genGameBoard(int[][] board, String playerSymbol) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        for (int i = 0; i < board.length; i++) {
            InlineKeyboardButton[] row = new InlineKeyboardButton[board[i].length];
            for (int j = 0; j < board[i].length; j++) {
                String symbol = switch (board[i][j]) {
                    case 1 -> playerSymbol;
                    case 2 -> (playerSymbol.equals(X_SIGN) ? O_SIGN : X_SIGN);
                    default -> EMPTY_SIGN;
                };
                row[j] = new InlineKeyboardButton(symbol)
                        .callbackData("cell_" + i + "_" + j);
            }
            markup.addRow(row);
        }
        return markup;
    }

    public InlineKeyboardMarkup genDifficultyLevelButtons(){
        return new InlineKeyboardMarkup()
                .addRow(
                        new InlineKeyboardButton(rm.getString(LEVEL_EASY)).callbackData(LEVEL + Difficulty.EASY)
                )
                .addRow(
                        new InlineKeyboardButton(rm.getString(LEVEL_AVERAGE)).callbackData(LEVEL + Difficulty.MEDIUM),
                        new InlineKeyboardButton(rm.getString(LEVEL_DIFFICULT)).callbackData(LEVEL + Difficulty.HARD)
                )
                .addRow(
                        new InlineKeyboardButton(rm.getString(LEVEL_EXTREME)).callbackData(LEVEL + Difficulty.EXTREME)
                )
                .addRow(
                        new InlineKeyboardButton(rm.getString(BACK_BUTTON_MSG)).callbackData("back_to_cabinet")
                );
    }

    public InlineKeyboardMarkup genLanguageButtons() {
        return new InlineKeyboardMarkup()
                .addRow(
                        new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDFFUzbek").callbackData("lang_uz"),
                        new InlineKeyboardButton("\uD83C\uDDF7\uD83C\uDDFAРусский").callbackData("lang_ru"),
                        new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDF8English").callbackData("lang_en")
                )
                .addRow(new InlineKeyboardButton(rm.getString(BACK_BUTTON_MSG))
                        .callbackData("back_to_cabinet")
                );
    }

    public InlineKeyboardMarkup genStartLanguageButtons() {
        return new InlineKeyboardMarkup()
                .addRow(
                        new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDFFUzbek").callbackData("lang_uz"),
                        new InlineKeyboardButton("\uD83C\uDDF7\uD83C\uDDFAРусский").callbackData("lang_ru"),
                        new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDF8English").callbackData("lang_en")
                );
    }

    public InlineKeyboardMarkup chooseSymbolButtons() {
        return new InlineKeyboardMarkup()
                .addRow(
                        new InlineKeyboardButton(X_SIGN).callbackData("CHOOSE_X"),
                        new InlineKeyboardButton(O_SIGN).callbackData("CHOOSE_O")
                )
                .addRow(
                        new InlineKeyboardButton(rm.getString(BACK_BUTTON_MSG)).callbackData("back_to_cabinet")
                );
    }

    public InlineKeyboardMarkup getBoardBtns(long gameId, int[][] board) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        for (int i = 0; i < board.length; i++) {
            InlineKeyboardButton[] row = new InlineKeyboardButton[board[i].length];
            for (int j = 0; j < board[i].length; j++) {
                String cellText = switch (board[i][j]) {
                    case 1 -> "❌";
                    case 2 -> "⭕";
                    default -> "⬜";
                };
                row[j] = new InlineKeyboardButton(cellText)
                        .callbackData("MOVE_" + gameId + "_" + i + "_" + j);
            }
            markup.addRow(row);
        }
        return markup;
    }

    public InlineKeyboardMarkup endMultiGameBtns() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("🔄").switchInlineQueryCurrentChat(" "),
                new InlineKeyboardButton("🤖").url("https://t.me/" + "xoDemoBot")
        );
    }



    public InlineKeyboardMarkup gameBoard(Long gameId, int[][] board) {
        List<InlineKeyboardButton[]> rows = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            InlineKeyboardButton[] row = new InlineKeyboardButton[3];
            for (int j = 0; j < 3; j++) {
                String symbol = board[i][j] == 1 ? "❌" : board[i][j] == 2 ? "⭕" : "⬜";
                row[j] = new InlineKeyboardButton(symbol).callbackData("MOVE_" + gameId + "_" + i + "_" + j);
            }
            rows.add(row);
        }

        return new InlineKeyboardMarkup(rows.toArray(new InlineKeyboardButton[0][]));
    }


}
