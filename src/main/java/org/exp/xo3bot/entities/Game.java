package org.exp.xo3bot.entities;

import jakarta.persistence.*;
import lombok.*;
import org.exp.xo3bot.entities.stats.Difficulty;
import org.exp.xo3bot.entities.stats.GameStatus;


//@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Game extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private GameStatus status = GameStatus.CREATED;

    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "player_symbol")
    private String playerSymbol;

    @Column(name = "bot_symbol")
    private String botSymbol;

    @Column(name = "game_board")
    private byte[] gameBoard;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    public Game(User user) {
        this.user = user;
    }
}
