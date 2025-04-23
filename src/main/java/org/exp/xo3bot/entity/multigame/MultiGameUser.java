package org.exp.xo3bot.entity.multigame;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "multi_game_users")
public class MultiGameUser {
    @Id
    private Long id;
    private String fullname;
    private String username;
    private boolean blocked = false;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "language_code")
    private String languageCode;

    /*
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MultiGameResult> results = new ArrayList<>();
    */
}
