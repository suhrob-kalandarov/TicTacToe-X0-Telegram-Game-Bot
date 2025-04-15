package org.exp.xo3bot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.exp.xo3bot.entities.stats.Difficulty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "results")
public class Result extends BaseEntity {

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
