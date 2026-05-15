package br.com.projeto_letramento.projeto_letramento.controller;

import br.com.projeto_letramento.projeto_letramento.model.Difficulty;
import br.com.projeto_letramento.projeto_letramento.model.Game;
import br.com.projeto_letramento.projeto_letramento.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/embaralhar/professor")
@RequiredArgsConstructor
public class EmbaralharProfessorController {
    private final GameService gameService;

    private Map<Long, String> buildImageMap(List<Game> games) {
        return games.stream()
                .collect(Collectors.toMap(Game::getId, game -> Base64.getEncoder().encodeToString(game.getImage())));
    }

    @GetMapping
    public String professorPage(Model model) {
        List<Game> games = gameService.getAllActiveGames();
        model.addAttribute("games", games);
        model.addAttribute("imageMap", buildImageMap(games));
        return "professor";
    }

    @PostMapping("/criar-jogo")
    public String criarJogo(
            @RequestParam("palavra") String palavra,
            @RequestParam("imagem") MultipartFile imagem,
            @RequestParam("dificuldade") Difficulty dificuldade,
            Model model) {
        
        try {
            if (imagem == null || imagem.isEmpty()) {
                model.addAttribute("erro", "Por favor, selecione uma imagem");
                List<Game> games = gameService.getAllActiveGames();
                model.addAttribute("games", games);
                model.addAttribute("imageMap", buildImageMap(games));
                return "professor";
            }

            if (palavra == null || palavra.trim().isEmpty()) {
                model.addAttribute("erro", "Por favor, digite uma palavra");
                List<Game> games = gameService.getAllActiveGames();
                model.addAttribute("games", games);
                model.addAttribute("imageMap", buildImageMap(games));
                return "professor";
            }

            byte[] imageBytes = imagem.getBytes();
            Game game = gameService.createGame(palavra.trim(), imageBytes, imagem.getOriginalFilename(), dificuldade);
            
            model.addAttribute("sucesso", "Jogo criado com sucesso!");
            List<Game> games = gameService.getAllActiveGames();
            model.addAttribute("games", games);
            model.addAttribute("imageMap", buildImageMap(games));
            
            return "professor";
        } catch (IOException e) {
            System.err.println("Erro IO ao criar jogo: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao processar a imagem: " + e.getMessage());
            List<Game> games = gameService.getAllActiveGames();
            model.addAttribute("games", games);
            model.addAttribute("imageMap", buildImageMap(games));
            return "professor";
        } catch (Exception e) {
            System.err.println("Erro ao criar jogo: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao criar jogo: " + e.getMessage());
            List<Game> games = gameService.getAllActiveGames();
            model.addAttribute("games", games);
            model.addAttribute("imageMap", buildImageMap(games));
            return "professor";
        }
    }

    @PostMapping("/desativar/{id}")
    public String desativarJogo(@PathVariable Long id) {
        gameService.deactivateGame(id);
        return "redirect:/embaralhar/professor";
    }

    @GetMapping("/detalhes/{id}")
    public String detalheJogo(@PathVariable Long id, Model model) {
        Game game = gameService.getGameById(id).orElse(null);
        if (game != null) {
            model.addAttribute("game", game);
            model.addAttribute("tentativas", gameService.getGameAttempts(id));
            model.addAttribute("imageBase64", Base64.getEncoder().encodeToString(game.getImage()));
        }
        return "detalhes-jogo";
    }
}