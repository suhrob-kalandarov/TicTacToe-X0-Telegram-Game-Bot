package org.exp.xo3bot.repos;

import org.exp.xo3bot.entities.MultiGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MultiGameRepository extends JpaRepository<MultiGame, Long> {

}
