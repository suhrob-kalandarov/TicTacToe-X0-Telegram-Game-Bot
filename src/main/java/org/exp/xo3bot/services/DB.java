package org.exp.xo3bot.services;

import org.exp.xo3bot.entities.stats.Difficulty;

public interface DB {


    static int getUserGameStat(long userId, Difficulty difficulty, String msg) {
        return 0;
    }

    static void updateGameScore(Long id, Difficulty difficulty, String winCount, int winCount1) {

    }
}
