package org.exp.xo3bot.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.exp.xo3bot.entities.enums.Language;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String fullname;
    private String username;
    private Language language;

    @Column(name = "language_code")
    private String languageCode;

    @Column(name = "game_id")
    private Long gameId;

    @Column(nullable = false)
    private boolean blocked = false;
}
