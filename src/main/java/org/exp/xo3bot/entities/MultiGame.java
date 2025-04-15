package org.exp.xo3bot.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.exp.xo3bot.entities.stats.GameStatus;


//@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

    @Column(name = "game_board")
    private byte[] gameBoard;
}
