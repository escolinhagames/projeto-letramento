package br.com.projeto_letramento.projeto_letramento.infra.security;


import br.com.projeto_letramento.projeto_letramento.model.ProfessorModel;
import br.com.projeto_letramento.projeto_letramento.repository.ProfessorRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomProfessorDetailsService implements UserDetailsService {
    CustomProfessorDetailsService(ProfessorRepository professorRepository){
        this.professorRepository = professorRepository;
    }
    private ProfessorRepository professorRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ProfessorModel professorModel = this.professorRepository.findOptionalByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(professorModel.getEmail(), professorModel.getSenha(), new ArrayList<>());
    }
}