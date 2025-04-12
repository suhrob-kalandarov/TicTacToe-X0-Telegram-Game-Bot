package org.exp.xo3bot.repos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.exp.xo3bot.entities.User;
import org.exp.xo3bot.usekeys.CustomRepository;

import java.util.List;

public class UserRepository implements CustomRepository<User, Long> {
    @PersistenceContext
    private EntityManager em;


    @Override
    public User findById(Long aLong) {
        return null;
    }

    @Override
    public void save(User entity) {

    }

    @Override
    public List<User> findAll() {
        return List.of();
    }
}