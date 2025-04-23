package org.exp.xo3bot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.exp.xo3bot.entity.stats.UserLevel;

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

    @Column(name = "user_level")
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel;

    private Integer experience = 100;
}
