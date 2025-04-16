package org.exp.xo3bot.services;

import com.pengrad.telegrambot.request.EditMessageText;
import org.exp.xo3bot.dtos.MainDto;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public void warningSender(MainDto dto, Long userId) {
        dto.getTelegramBot().execute(new EditMessageText(
                userId,
                dto.getUserRepository().getById(userId).getGame().getMessageId(),
                ""
        ));
    }
}
