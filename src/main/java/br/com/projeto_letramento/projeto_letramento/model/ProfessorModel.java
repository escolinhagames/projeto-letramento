package br.com.projeto_letramento.projeto_letramento.model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name="professores",schema="turma")
public class ProfessorModel implements Serializable{
    
    @Id
    @GeneratedValue(generator = "turma.professores_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="turma.professores_seq", sequenceName = "turma.professores_seq", allocationSize = 1)
    @Column(name="id")
    private Integer id;
    @Column(name="nome")
    private String nome;
    @Column(name="email")
    private String email;
    @Column(name="senha")
    private String senha;
    @CreationTimestamp
    @Column(name="data_hr_criacao")
    private LocalDateTime dataHrCriacao;

    public ProfessorModel(){
        
    }

    public ProfessorModel(Integer id, String nome, String email, String senha, LocalDateTime dataHrCriacao){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataHrCriacao = dataHrCriacao;
    }
    
    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getSenha(){
        return senha;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public LocalDateTime getDataHrCriacao(){
        return dataHrCriacao;
    }

    public void setDataHrCriacao(LocalDateTime dataHrCriacao){
        this.dataHrCriacao = dataHrCriacao;
    }
    
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null|| getClass() != o.getClass()) return false;
        ProfessorModel professorModel = (ProfessorModel) o;
        return Objects.equals(id, professorModel.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }


}
