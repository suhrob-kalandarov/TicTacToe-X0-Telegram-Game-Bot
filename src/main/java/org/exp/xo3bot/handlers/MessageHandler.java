package org.exp.xo3bot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exp.xo3bot.entities.Game;
import org.exp.xo3bot.entities.User;
import org.exp.xo3bot.entities.stats.Language;
import org.exp.xo3bot.repos.UserRepository;
import org.exp.xo3bot.services.BotButtons;
import org.exp.xo3bot.services.ResourceMessageManager;
import org.exp.xo3bot.usekeys.Handler;
import org.exp.xo3bot.processes.CabinetCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;

import static org.exp.xo3bot.utils.Constants.WARNING_MSG;

@RequiredArgsConstructor
@Component
public class MessageHandler implements Handler<Message> {

    @Autowired
    private final TelegramBot telegramBot;

    @Autowired
    private final ResourceMessageManager rm;

    @Autowired
    private final BotButtons buttons;

    @Autowired
    private final UserRepository userRepo;

    private static final Logger logger = LogManager.getLogger(MessageHandler.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void handle(Message message) {
        Runnable process = null;
        User user;
        String text = message.text();

        if (!userRepository.existsById(message.from().id())){
            user = User.builder()
                    .id(message.from().id())
                    .fullname(message.from().firstName() + " " + message.from().lastName())
                    .username(message.from().username())
                    .language(Language.EN)
                    .languageCode(message.from().languageCode())
                    .build();
            user.setGame(new Game(user));

            rm.setLocale(new Locale("ru"));

            userRepository.save(user);
        } else {
            user = userRepo.getById(message.from().id());
        }

        if (text != null){

            if (text.equals("/start")) {
                process = new CabinetCmd(telegramBot, user, rm, buttons);

            } else if (text.equals("/")) {


            }

        } else {
            telegramBot.execute(new DeleteMessage(
                    user.getId(),
                    user.getGame().getMessageId())
            );
            int newMessageId = telegramBot.execute(
                    new SendMessage(user.getId(), rm.getString(WARNING_MSG))
            ).message().messageId();
            user.getGame().setMessageId(newMessageId);

            //new MainMenuCmd(user).process();
        }

        Objects.requireNonNull(process).run();
    }
}
