package org.exp.xo3bot.processes;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entity.User;
import org.exp.xo3bot.utils.Constants;


@RequiredArgsConstructor
public class CabinetCmd implements Runnable {

    private final User user;
    private final MainDto dto;

    private static final Logger logger = LogManager.getLogger(CabinetCmd.class);

    @Override
    public void run() {
        logger.info("Kabinet menyusi ko'rsatilmoqda (User: {})", user.getId());
        try {

            if (user.getGame().getMessageId() != null) {
                new EditMessageText(user.getId(), user.getGame().getMessageId(), "<<<");
            }

            SendResponse response =  dto.getTelegramBot().execute(
                    new SendMessage(
                            user.getId(),
                            dto.getRm().getString(Constants.START_MSG)

                    ).replyMarkup(dto.getButtons().genCabinetButtons())
                            .parseMode(ParseMode.HTML)
            );

            user.getGame().setMessageId(response.message().messageId());
            dto.getUserRepository().save(user);

            logger.debug("Kabinet menyusi yuborildi");

        } catch (Exception e) {
            logger.error("Kabinet menyusini yuborishda xatolik: {}", e.getMessage(), e);
        }
    }
}

