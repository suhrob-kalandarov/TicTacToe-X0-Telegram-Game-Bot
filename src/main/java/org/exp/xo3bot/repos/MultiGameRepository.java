package org.exp.xo3bot.repos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.exp.xo3bot.entities.MultiGame;
import org.exp.xo3bot.usekeys.CustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class MultiGameRepository implements CustomRepository<MultiGame, Long> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public MultiGame findById(Long aLong) {
        return null;
    }

    @Override
    public void save(MultiGame entity) {
        em.persist(entity);
    }

    @Override
    public List<MultiGame> findAll() {
        return List.of();
    }
}
