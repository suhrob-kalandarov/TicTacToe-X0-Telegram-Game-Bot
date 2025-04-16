package org.exp.xo3bot.entities;

import jakarta.persistence.*;
import lombok.*;
import org.exp.xo3bot.entities.stats.Level;

//@EqualsAndHashCode(callSuper = true)
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
    private Level level;

}
