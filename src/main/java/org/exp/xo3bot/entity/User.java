package org.exp.xo3bot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.exp.xo3bot.entity.botgame.BotGame;
import org.exp.xo3bot.entity.botgame.BotGameResult;
import org.exp.xo3bot.entity.stats.Language;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    private Long id;

    //@Column(nullable = false)
    private String fullname;

    //@Column(nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    private Language language;

    @Column(name = "language_code")
    private String languageCode;

    //@Column(nullable = false)
    private boolean blocked = false;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private BotGame game;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BotGameResult> results = new ArrayList<>();

    /*
    @PostPersist
    public void createResults() {
        // Foydalanuvchi uchun har bir qiyinlik darajasi uchun Result yozuvlarini yaratish
        for (Difficulty difficulty : Difficulty.values()) {
            this.results.add(
                    Achievement.builder()
                            .difficulty(difficulty)
                            .winCount(0)
                            .loseCount(0)
                            .drawCount(0)
                            .user(this)
                            .build()
            );
        }
    }
    */
}