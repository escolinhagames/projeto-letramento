package br.com.projeto_letramento.projeto_letramento.repository;

import br.com.projeto_letramento.projeto_letramento.model.JogoMatematicaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JogoMatematicaRepository extends JpaRepository<JogoMatematicaModel, Long> {
    List<JogoMatematicaModel> findByAtivaTrue();
    List<JogoMatematicaModel> findByProfessorIdAndAtivaTrue(Integer professorId);
}