package org.exp.xo3bot.entity.botgame;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.exp.xo3bot.entity.BaseEntity;
import org.exp.xo3bot.entity.User;
import org.exp.xo3bot.entity.stats.Difficulty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "results")
public class BotGameResult extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(name = "win_count")
    private int winCount;

    @Column(name = "lose_count")
    private int loseCount;

    @Column(name = "draw_count")
    private int drawCount;
}
