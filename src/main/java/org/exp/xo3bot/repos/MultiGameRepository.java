package org.exp.xo3bot.repos;

import org.exp.xo3bot.entity.MultiGame;
import org.exp.xo3bot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MultiGameRepository extends JpaRepository<MultiGame, Long> {

    @Query("SELECT m.playerX FROM MultiGame m WHERE m.inlineMessageId = :inlineMessageId")
    User findUserByInlineMessageId(@Param("inlineMessageId") String inlineMessageId);


    @Query("""
            SELECT m FROM MultiGame m 
            WHERE m.status IN ('DEAD', 'CREATED', 'DEAD_LOCK', 'IDLE')
            ORDER BY m.id ASC
            LIMIT 1
        """)
    Optional<MultiGame> findFirstByStatus();

}
