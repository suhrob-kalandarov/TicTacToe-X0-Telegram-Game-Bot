package org.exp.xo3bot.processes.callbackquery;

import com.pengrad.telegrambot.request.EditMessageText;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entities.User;
import org.exp.xo3bot.entities.stats.Language;
import org.exp.xo3bot.processes.CabinetCmd;
import java.util.Locale;

@RequiredArgsConstructor
public class LanguageChangerMenuCmd implements Runnable {
    private final User user;
    private final String data;
    private final MainDto dto;

    @Override
    public void run() {
        String[] parts = data.split("_");

        if (parts.length > 1) {
            String languageCode = parts[1];

            user.setLanguage(Language.valueOf(languageCode.toUpperCase()));
            dto.getUserRepository().save(user);
            dto.getRm().setLocale(new Locale(languageCode));

            new CabinetCmd(user, dto).run();
        }
    }
}
