package org.exp.xo3bot.dtos;

import com.pengrad.telegrambot.TelegramBot;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exp.xo3bot.entities.MultiGame;
import org.exp.xo3bot.handlers.MessageHandler;
import org.exp.xo3bot.repos.GameRepository;
import org.exp.xo3bot.repos.MultiGameRepository;
import org.exp.xo3bot.repos.ResultRepository;
import org.exp.xo3bot.repos.UserRepository;
import org.exp.xo3bot.services.BotButtons;
import org.exp.xo3bot.services.ResourceMessageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@RequiredArgsConstructor
@Component
public class MainDto {

    @Autowired
    private final TelegramBot telegramBot;

    @Autowired
    private final ResourceMessageManager rm;

    @Autowired
    private final BotButtons buttons;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final GameRepository gameRepository;

    @Autowired
    private final MultiGameRepository multiGameRepository;

    @Autowired
    private final ResultRepository resultRepository;


    //private static final Logger logger = LogManager.getLogger();

}
