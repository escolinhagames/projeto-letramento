package br.com.projeto_letramento.projeto_letramento.repository;

import br.com.projeto_letramento.projeto_letramento.model.Player;
import br.com.projeto_letramento.projeto_letramento.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByRoomOrderByPlayerNumber(Room room);
    int countByRoom(Room room);
}