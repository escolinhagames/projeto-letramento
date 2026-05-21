package br.com.projeto_letramento.projeto_letramento.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "palavras")
public class PalavraModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "palavra",  length = 512)
    private String palavra;

    @Column(name = "imagem", length = 500)
    private String imagem;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private ProfessorModel professor;

    private Integer excluido = 0;

    @Column(name = "dataHoraAtualizacao", nullable = true)
    private LocalDateTime dataHoraAtualizacao;

    @PrePersist
    @PreUpdate
    public void atualizarData() {
        this.dataHoraAtualizacao = LocalDateTime.now();
    }
}