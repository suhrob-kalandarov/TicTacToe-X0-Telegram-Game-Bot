package org.exp.xo3bot.repos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.exp.xo3bot.entities.User;
import org.exp.xo3bot.usekeys.CustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class myUserRepository implements CustomRepository<User, Long> {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void save(User entity) {
        em.persist(entity);
    }

    @Override
    public User findById(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }


    public boolean existsByUsername(String username) {
        Long count = em.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }

}