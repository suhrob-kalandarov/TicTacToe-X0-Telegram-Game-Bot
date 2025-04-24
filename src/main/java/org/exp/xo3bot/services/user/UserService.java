package org.exp.xo3bot.services.user;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.ChosenInlineResult;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;

import org.exp.xo3bot.entity.*;
import org.exp.xo3bot.entity.botgame.BotGame;
import org.exp.xo3bot.entity.botgame.BotGameResult;
import org.exp.xo3bot.entity.multigame.MultiGame;
import org.exp.xo3bot.entity.stats.Difficulty;
import org.exp.xo3bot.entity.stats.Language;
import org.exp.xo3bot.repos.UserRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getOrCreateTelegramUser(Message message) {
       // Message message = update.message();

        if (message != null) {
            Long id = message.from().id();
            Optional<User> optionalUser = userRepository.findById(id);

            if (optionalUser.isPresent()) {
                return optionalUser.get();
            } else {
                User user = User.builder()
                        .id(id)
                        .fullname(buildFullNameFromUpdate(message))
                        .username(message.from().username())
                        .languageCode(message.from().languageCode())
                        .language(Language.EN)
                        .build();

                BotGame game = createGameRow(user);
                //MultiGame multiGame = createMultiGameRow(user);
                //Achievement achievement = createAchievementRow(user);
                List<BotGameResult> results = createUserResultRows(user);


                user.setGame(game);

                user.setResults(results);

                return userRepository.save(user);
            }
        }

        return null;
    }

    private Achievement createAchievementRow(User user) {
        return null;
    }

    private MultiGame createMultiGameRow(User user) {
        return null;
    }

    public User getOrCreateTelegramUser(CallbackQuery callbackQuery){
        if (callbackQuery != null) {
            Long id = callbackQuery.from().id();
            Optional<User> optionalUser = userRepository.findById(id);
            return optionalUser.orElse(null);
        }
        return null;
    }


    public User getOrCreateTelegramUser(ChosenInlineResult chosenInlineResult){
        Long id = chosenInlineResult.from().id();
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = User.builder()
                .id(id)
                .fullname(buildFullNameFromUpdate(chosenInlineResult))
                .username(chosenInlineResult.from().username())
                .languageCode(chosenInlineResult.from().languageCode())
                .language(Language.EN)
                .blocked(true)
                .build();

        //BotGame game = createGameRow(user);
        //MultiGame multiGame = createMultiGameRow(user);
        //Achievement achievement = createAchievementRow(user);
        //List<Result> results = createUserResultRows(user);

        //user.setGame(game);
        //user.setResults(results);

        return userRepository.save(user);
    }

    private List<BotGameResult> createUserResultRows(User user) {
        List<BotGameResult> results = new ArrayList<>();

        for (Difficulty difficulty : Difficulty.values()) {
            BotGameResult result = BotGameResult.builder()
                    .user(user)
                    .difficulty(difficulty)
                    .winCount(0)
                    .loseCount(0)
                    .drawCount(0)
                    .build();

            results.add(result);
        }

        return results;
    }




    private BotGame createGameRow(User user) {
        return new BotGame(user); // constructor ichida board init bo‘lsa yaxshi
    }

    public String generateDefaultUsername() {
        long i = 1;
        String username;

        do {
            username = "user" + i;
            i++;
        } while (userRepository.existsByUsername(username));

        return username;
    }

    public String buildFullNameFromUpdate(Message message) {
        String firstName = message.chat().firstName();
        String lastName = message.chat().lastName();

        if (firstName == null && lastName == null) {
            return generateDefaultUsername();
        }

        if (firstName == null) return lastName;
        if (lastName == null) return firstName;

        return firstName + " " + lastName;
    }


    public String buildFullNameFromUpdate(CallbackQuery callbackQuery) {
        String firstName = callbackQuery.from().firstName();
        String lastName = callbackQuery.from().lastName();

        if (firstName == null && lastName == null) {
            return generateDefaultUsername();
        }

        if (firstName == null) return lastName;
        if (lastName == null) return firstName;

        return firstName + " " + lastName;
    }

    public String buildFullNameFromUpdate(ChosenInlineResult chosenInlineResult) {
        String firstName = chosenInlineResult.from().firstName();
        String lastName = chosenInlineResult.from().lastName();

        if (firstName == null && lastName == null) {
            return generateDefaultUsername();
        }

        if (firstName == null) return lastName;
        if (lastName == null) return firstName;

        return firstName + " " + lastName;
    }

    public String buildFullNameFromUpdate(InlineQuery inlineQuery) {
        String firstName = inlineQuery.from().firstName();
        String lastName = inlineQuery.from().lastName();

        if (firstName == null && lastName == null) {
            return generateDefaultUsername();
        }

        if (firstName == null) return lastName;
        if (lastName == null) return firstName;

        return firstName + " " + lastName;
    }
}
