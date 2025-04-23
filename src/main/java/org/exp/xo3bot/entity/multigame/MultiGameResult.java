/*
package org.exp.xo3bot.entity.multigame;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.exp.xo3bot.entity.BaseEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MultiGameResult extends BaseEntity {

   */
/*
    @ManyToOne
    @JoinColumn(name = "multi_game_user_id")
    private User user;
   *//*


    @Column(name = "win_count")
    private int winCount;

    @Column(name = "lose_count")
    private int loseCount;

    @Column(name = "draw_count")
    private int drawCount;
}
*/
