package br.com.projeto_letramento.projeto_letramento.service;

import br.com.projeto_letramento.projeto_letramento.model.Difficulty;
import br.com.projeto_letramento.projeto_letramento.model.Game;
import br.com.projeto_letramento.projeto_letramento.model.GameAttempt;
import br.com.projeto_letramento.projeto_letramento.repository.GameRepository;
import br.com.projeto_letramento.projeto_letramento.repository.GameAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final GameAttemptRepository gameAttemptRepository;

    public Game createGame(String word, byte[] image, String imageName, Difficulty difficulty) {
        Game game = new Game();
        game.setWord(word.toLowerCase().trim());
        game.setImage(image);
        game.setImageName(imageName);
        game.setDifficulty(difficulty);
        game.setCreatedAt(LocalDateTime.now());
        game.setActive(true);
        return gameRepository.save(game);
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }

    public List<Game> getAllActiveGames() {
        return gameRepository.findByActiveTrue();
    }

    public GameAttempt saveAttempt(Long gameId, String studentAnswer) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        
        if (gameOptional.isEmpty()) {
            throw new IllegalArgumentException("Jogo não encontrado");
        }

        Game game = gameOptional.get();
        boolean isCorrect = game.getWord().equalsIgnoreCase(studentAnswer.trim());

        GameAttempt attempt = new GameAttempt();
        attempt.setGame(game);
        attempt.setStudentAnswer(studentAnswer.toLowerCase().trim());
        attempt.setCorrect(isCorrect);
        attempt.setAttemptedAt(LocalDateTime.now());

        return gameAttemptRepository.save(attempt);
    }

    public List<GameAttempt> getGameAttempts(Long gameId) {
        return gameAttemptRepository.findByGameIdOrderByAttemptedAtDesc(gameId);
    }

    public void deactivateGame(Long gameId) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            game.setActive(false);
            gameRepository.save(game);
        }
    }
    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }
}