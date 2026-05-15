package br.com.projeto_letramento.projeto_letramento.repository;

import br.com.projeto_letramento.projeto_letramento.model.GameAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GameAttemptRepository extends JpaRepository<GameAttempt, Long> {
    List<GameAttempt> findByGameIdOrderByAttemptedAtDesc(Long gameId);
}