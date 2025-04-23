package org.exp.xo3bot.repos;


import org.exp.xo3bot.entity.botgame.BotGameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<BotGameResult, Long> {

    List<BotGameResult> findByUser_Id(Long userId);
}
