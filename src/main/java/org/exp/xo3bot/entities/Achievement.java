package org.exp.xo3bot.entities;

import jakarta.persistence.*;
import lombok.*;
import org.exp.xo3bot.entities.stats.Difficulty;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "achievements")
public class Achievement extends BaseEntity {

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
