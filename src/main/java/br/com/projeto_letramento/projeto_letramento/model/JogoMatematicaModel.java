package br.com.projeto_letramento.projeto_letramento.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "jogo_matematica")
public class JogoMatematicaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dificuldade;
    private Integer professorId;
    private boolean ativa = true;
    private Integer maxAcertos = 5;
    private Integer maxErros = 3;

    @CreationTimestamp
    private LocalDateTime criadoEm;
}