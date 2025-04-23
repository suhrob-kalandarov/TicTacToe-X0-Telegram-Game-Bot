package org.exp.xo3bot.repos;

import org.exp.xo3bot.entity.multigame.MultiGameUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultiGameUserRepository extends JpaRepository<MultiGameUser, Long> {

}
