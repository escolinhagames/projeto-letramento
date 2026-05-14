package br.com.projeto_letramento.projeto_letramento.repository;

import br.com.projeto_letramento.projeto_letramento.model.JogoImagemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface JogoImagemRepository extends JpaRepository<JogoImagemModel, Long> {
    List<JogoImagemModel> findByAtivaTrue();
    List<JogoImagemModel> findByProfessorIdAndAtivaTrue(Integer professorId);
    List<JogoImagemModel> findByCriadoEmBefore(LocalDateTime data);
}