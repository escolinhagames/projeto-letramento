package br.com.projeto_letramento.projeto_letramento.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "jogo_imagem")
public class JogoImagemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String palavraCorreta;
    private String dificuldade; // "FACIL", "MEDIO", "DIFICIL"

    @Column(name = "imagem1")
    private byte[] imagem1;

    @Column(name = "imagem2")
    private byte[] imagem2;

    @Column(name = "imagem3")
    private byte[] imagem3;

    private Integer professorId;
    private boolean ativa = true;

    @CreationTimestamp
    private LocalDateTime criadoEm;
}