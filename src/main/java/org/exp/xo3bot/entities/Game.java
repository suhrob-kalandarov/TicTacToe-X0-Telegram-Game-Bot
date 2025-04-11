package org.exp.xo3bot.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import org.exp.xo3bot.entities.enums.DifficultyLevel;
import org.exp.xo3bot.entities.enums.GameStatus;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Game extends BaseEntity {

    private GameStatus status;

    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "player_symbol")
    private String playerSymbol;

    @Column(name = "bot_symbol")
    private String botSymbol;

    @Column(name = "game_board")
    private byte[] gameBoard;

    @Column(name = "difficulty_level")
    private DifficultyLevel difficultyLevel;
}
