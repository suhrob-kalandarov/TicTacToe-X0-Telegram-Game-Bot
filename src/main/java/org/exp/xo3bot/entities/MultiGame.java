package org.exp.xo3bot.entities;

import jakarta.persistence.*;
import lombok.*;
import org.exp.xo3bot.entities.stats.Difficulty;
import org.exp.xo3bot.entities.stats.GameStatus;
import org.exp.xo3bot.services.GameBoardConverter;


//@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "multi_games")
public class MultiGame extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "inline_message_id")
    private Integer inlineMessageId;

    @Column(name = "player_x")
    private Long playerX;

    @Column(name = "player_o")
    private Long playerO;

    @Convert(converter = GameBoardConverter.class)
    @Column(name = "game_board", length = 50)
    private int[][] gameBoard;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    public void initializeBoard() {
        this.gameBoard = new int[3][3];
    }


}
