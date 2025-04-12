package org.exp.xo3bot.repos;

import org.exp.xo3bot.entities.Game;
import org.exp.xo3bot.usekeys.CustomRepository;

import java.util.List;


public class GameRepository implements CustomRepository<Game, Long> {

    @Override
    public Game findById(Long aLong) {
        return null;
    }

    @Override
    public void save(Game entity) {

    }

    @Override
    public List<Game> findAll() {
        return List.of();
    }
}
