package org.exp.xo3bot.repos;

import org.exp.xo3bot.entities.MultiGame;
import org.exp.xo3bot.usekeys.CustomRepository;

import java.util.List;


public class MultiGameRepository implements CustomRepository<MultiGame, Long> {

    @Override
    public MultiGame findById(Long aLong) {
        return null;
    }

    @Override
    public void save(MultiGame entity) {

    }

    @Override
    public List<MultiGame> findAll() {
        return List.of();
    }
}
