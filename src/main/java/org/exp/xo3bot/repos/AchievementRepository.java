package org.exp.xo3bot.repos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.exp.xo3bot.entities.Achievement;
import org.exp.xo3bot.usekeys.CustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class AchievementRepository implements CustomRepository<Achievement, Long> {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Achievement findById(Long aLong) {
        return null;
    }

    @Override
    public void save(Achievement entity) {
        em.persist(entity);
    }

    @Override
    public List<Achievement> findAll() {
        return List.of();
    }
}
