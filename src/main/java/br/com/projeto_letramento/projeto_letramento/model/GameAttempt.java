package br.com.projeto_letramento.projeto_letramento.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(nullable = false)
    private String studentAnswer;

    @Column(nullable = false)
    private Boolean correct;

    @Column(nullable = false)
    private LocalDateTime attemptedAt;
}