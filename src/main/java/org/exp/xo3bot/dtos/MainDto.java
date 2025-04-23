package org.exp.xo3bot.dtos;

import com.pengrad.telegrambot.TelegramBot;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.repos.*;
import org.exp.xo3bot.services.base.BotButtons;
import org.exp.xo3bot.services.base.GameBoardService;
//import org.exp.xo3bot.services.botgame.BotGameService;
import org.exp.xo3bot.services.msg.ResourceMessageManager;
import org.exp.xo3bot.services.multigame.MultiGameLogic;
import org.exp.xo3bot.services.multigame.MultiGameService;
import org.exp.xo3bot.services.user.UserService;
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
    private final UserService userService;

    @Autowired
    private final GameRepository gameRepository;

    @Autowired
    private final MultiGameRepository multiGameRepository;

    @Autowired
    private final ResultRepository resultRepository;

    @Autowired
    private final MultiGameService multiGameService;

    @Autowired
    private final MultiGameUserRepository multiGameUserRepository;

    @Autowired
    private final MultiGameLogic multiGameLogic;

    @Autowired
    private final GameBoardService gameBoardService;


    //@Autowired
    //private final BotGameService botGameService;


    //private static final Logger logger = LogManager.getLogger();

}
