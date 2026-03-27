package br.com.projeto_letramento.projeto_letramento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.projeto_letramento.projeto_letramento.model.ProfessorModel;

public interface ProfessorRepository extends JpaRepository<ProfessorModel, Integer>{
    
    //@Query("select p from ProfessorModel p whare p. ")
    ProfessorModel findByEmailAndSenha(String email, String senha);

    ProfessorModel findByEmail(String email);
}
