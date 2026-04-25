package br.com.projeto_letramento.projeto_letramento.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class LoginRequestDTO {
    private String email;
    private String senha;
    @JsonAlias("password")
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
