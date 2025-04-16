package org.exp.xo3bot.processes;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entities.User;
import org.exp.xo3bot.repos.UserRepository;
import org.exp.xo3bot.services.BotButtons;
import org.exp.xo3bot.services.ResourceMessageManager;

import static org.exp.xo3bot.utils.Constants.*;

@RequiredArgsConstructor
public class LanguageMenuCmd implements Runnable {

    private final User user;
    private final MainDto dto;

    private static final Logger logger = LogManager.getLogger(CabinetCmd.class);

    @Override
    public void run() {

        logger.info("Til tanlash menyusi ko'rsatilmoqda: (user{})", user.getId());
        try {
            SendResponse sendResponse = null;

            if (user.getGame().getMessageId() != null) {
                sendResponse = (SendResponse) dto.getTelegramBot().execute(
                        new EditMessageText(user.getId(), user.getGame().getMessageId(),
                                (CHOOSE_LANG)
                        ).replyMarkup(dto.getButtons().genLanguageButtons())
                );

            } else {
                sendResponse = dto.getTelegramBot().execute(
                        new SendMessage(user.getId(),
                                (CHOOSE_LANG_EN_MSG)
                        ).replyMarkup(dto.getButtons().genStartLanguageButtons())
                );
            }

            user.getGame().setMessageId(sendResponse.message().messageId());
            dto.getUserRepository().save(user);

            logger.debug("Til tanlash menyusi yuborildi: user{" + user.getId() + "}");

        } catch (Exception e) {
            logger.error("Til menyusini ko'rsatishda xatolik: (user{}), {}", user.getId(), e.getMessage(), e);
        }
    }
}
