package br.com.projeto_letramento.projeto_letramento.service;

import br.com.projeto_letramento.projeto_letramento.model.Player;
import br.com.projeto_letramento.projeto_letramento.model.Room;
import br.com.projeto_letramento.projeto_letramento.repository.PlayerRepository;
import br.com.projeto_letramento.projeto_letramento.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;

    public Room createRoom() {
        String code;
        do {
            code = generateRoomCode();
        } while (roomRepository.findByCodeAndActiveTrue(code).isPresent());

        Room room = new Room();
        room.setCode(code);
        room.setCreatedAt(LocalDateTime.now());
        room.setActive(true);
        return roomRepository.save(room);
    }

    public Optional<Room> findRoomByCode(String code) {
        return roomRepository.findByCodeAndActiveTrue(code.toUpperCase());
    }

    public Optional<Room> findRoomById(Long id) {
        return roomRepository.findByIdAndActiveTrue(id);
    }

    public Player joinRoom(Room room, String playerName) {
        int playerCount = playerRepository.countByRoom(room);
        int playerNumber = playerCount + 1;

        String finalName = (playerName == null || playerName.trim().isEmpty())
            ? "Jogador " + playerNumber
            : playerName.trim();

        Player player = new Player();
        player.setName(finalName);
        player.setRoom(room);
        player.setJoinedAt(LocalDateTime.now());
        player.setPlayerNumber(playerNumber);

        return playerRepository.save(player);
    }

    public List<Player> getPlayersInRoom(Room room) {
        return playerRepository.findByRoomOrderByPlayerNumber(room);
    }

    private String generateRoomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }

        return code.toString();
    }
}