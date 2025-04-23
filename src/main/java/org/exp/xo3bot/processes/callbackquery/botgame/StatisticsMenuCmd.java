package org.exp.xo3bot.processes.callbackquery.botgame;

import lombok.RequiredArgsConstructor;
import org.exp.xo3bot.dtos.MainDto;
import org.exp.xo3bot.entity.User;

@RequiredArgsConstructor
public class StatisticsMenuCmd implements Runnable {
    private final User user;
    private final MainDto dto;

    @Override
    public void run() {

    }
}
