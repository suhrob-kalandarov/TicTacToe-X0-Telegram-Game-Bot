package org.exp.xo3bot.processes;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exp.xo3bot.entities.User;
import org.exp.xo3bot.services.BotButtons;
import org.exp.xo3bot.services.ResourceMessageManager;
import org.exp.xo3bot.utils.Constants;


@RequiredArgsConstructor
public class CabinetCmd implements Runnable {

    private final TelegramBot telegramBot;
    private final User user;
    private final ResourceMessageManager rm;
    private final BotButtons buttons;

    private static final Logger logger = LogManager.getLogger(CabinetCmd.class);

    @Override
    public void run() {
        logger.info("Kabinet menyusi ko'rsatilmoqda (User: {})", user.getId());
        try {

            user.getGame().setMessageId(telegramBot.execute(
                    new SendMessage(
                            user.getId(),
                            rm.getString(Constants.START_MSG)
                    ).replyMarkup(buttons.genCabinetButtons())
                            .parseMode(ParseMode.HTML)
            ).message().messageId());

            //DB.updateMessageId(user.getUserId(), user.getMessageId());

            logger.debug("Kabinet menyusi yuborildi");
        } catch (Exception e) {
            logger.error("Kabinet menyusini yuborishda xatolik: {}", e.getMessage(), e);
        }
    }
}

