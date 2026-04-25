package br.com.projeto_letramento.projeto_letramento.repository;

import br.com.projeto_letramento.projeto_letramento.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByCodeAndActiveTrue(String code);
    Optional<Room> findByIdAndActiveTrue(Long id);
}