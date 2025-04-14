package org.exp.xo3bot.services;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.repos.myUserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final myUserRepository userRepository;

    public String generateDefaultUsername() {
        long i = 1;
        String username;

        do {
            username = "user" + i;
            i++;
        } while (userRepository.existsByUsername(username));

        return username;
    }

    public String buildFullNameFromUpdate(Update update) {
        String firstName = update.message().chat().firstName();
        String lastName = update.message().chat().lastName();

        if (firstName == null && lastName == null) {
            return generateDefaultUsername();
        }

        if (firstName == null) {
            return lastName;
        }

        if (lastName == null) {
            return firstName;
        }

        return firstName + " " + lastName;
    }
}
