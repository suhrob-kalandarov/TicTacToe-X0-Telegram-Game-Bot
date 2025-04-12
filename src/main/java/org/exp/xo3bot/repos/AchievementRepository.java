package org.exp.xo3bot.repos;

import org.exp.xo3bot.entities.Achievement;
import org.exp.xo3bot.usekeys.CustomRepository;

import java.util.List;


public class AchievementRepository implements CustomRepository<Achievement, Long> {

    @Override
    public Achievement findById(Long aLong) {
        return null;
    }

    @Override
    public void save(Achievement entity) {

    }

    @Override
    public List<Achievement> findAll() {
        return List.of();
    }
}
