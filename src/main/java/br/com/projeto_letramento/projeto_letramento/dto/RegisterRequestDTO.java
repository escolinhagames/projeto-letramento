package br.com.projeto_letramento.projeto_letramento.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class RegisterRequestDTO {
    private String nome;
    @JsonAlias("name")
    private String name;
    private String email;
    private String senha;
    @JsonAlias("password")
    private String password;

    public String getNome() {
        return nome != null ? nome : name;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha != null ? senha : password;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
