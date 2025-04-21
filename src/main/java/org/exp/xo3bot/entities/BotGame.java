package org.exp.xo3bot.entities;

import jakarta.persistence.*;
import lombok.*;
import org.exp.xo3bot.entities.stats.Difficulty;
import org.exp.xo3bot.entities.stats.GameStatus;
import org.exp.xo3bot.services.base.GameBoardConverter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bot_games")
public class BotGame extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private GameStatus status = GameStatus.CREATED;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "player_symbol")
    private String playerSymbol;

    @Column(name = "bot_symbol")
    private String botSymbol;

    @Convert(converter = GameBoardConverter.class)
    @Column(name = "game_board", length = 50)
    private int[][] gameBoard;

    public BotGame(User user) {
        this.user = user;
        this.gameBoard = new int[3][3];
    }

    public void initGameBoard() {
        this.gameBoard = new int[3][3];
    }
}
