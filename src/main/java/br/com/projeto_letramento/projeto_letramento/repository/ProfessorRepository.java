package br.com.projeto_letramento.projeto_letramento.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.projeto_letramento.projeto_letramento.model.ProfessorModel;
@Repository
public interface ProfessorRepository extends JpaRepository<ProfessorModel, Integer>{
    
    //@Query("select p from ProfessorModel p whare p. ")
    ProfessorModel findByEmail(String email);
    Optional<ProfessorModel> findOptionalByEmail(String email);
}
