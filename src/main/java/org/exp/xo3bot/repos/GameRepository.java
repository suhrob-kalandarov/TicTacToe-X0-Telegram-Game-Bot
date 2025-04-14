package org.exp.xo3bot.repos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.exp.xo3bot.entities.Game;
import org.exp.xo3bot.usekeys.CustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class GameRepository implements CustomRepository<Game, Long> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Game findById(Long aLong) {
        return null;
    }

    @Override
    public void save(Game entity) {
        em.persist(entity);
    }

    @Override
    public List<Game> findAll() {
        return List.of();
    }
}
