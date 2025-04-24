package org.exp.xo3bot.entity.multigame;

import jakarta.persistence.*;
import lombok.*;
import org.exp.xo3bot.entity.BaseEntity;
import org.exp.xo3bot.entity.stats.GameStatus;
import org.exp.xo3bot.services.base.GameBoardConverter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "multi_games")
public class MultiGame extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "in_turn")
    @Enumerated(EnumType.STRING)
    private Turn inTurn;

    @Column(name = "inline_message_id")
    private String inlineMessageId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_x")
    private MultiGameUser playerX;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_o")
    private MultiGameUser playerO;

    @Convert(converter = GameBoardConverter.class)
    @Column(name = "game_board", length = 50)
    private int[][] gameBoard;

    public void initGameBoard() {
        this.gameBoard = new int[3][3];
    }
}
