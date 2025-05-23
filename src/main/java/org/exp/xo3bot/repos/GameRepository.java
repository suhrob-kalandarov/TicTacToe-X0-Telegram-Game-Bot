package org.exp.xo3bot.repos;

import org.exp.xo3bot.entity.botgame.BotGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<BotGame, Long> {

}
