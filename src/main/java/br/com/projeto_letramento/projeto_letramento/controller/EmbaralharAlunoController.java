package br.com.projeto_letramento.projeto_letramento.controller;

import br.com.projeto_letramento.projeto_letramento.model.Game;
import br.com.projeto_letramento.projeto_letramento.model.GameAttempt;
import br.com.projeto_letramento.projeto_letramento.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/embaralhar/aluno")
@RequiredArgsConstructor
public class EmbaralharAlunoController {
    private final GameService gameService;

    private Map<Long, String> buildImageMap(List<Game> games) {
        return games.stream()
                .collect(Collectors.toMap(Game::getId, game -> Base64.getEncoder().encodeToString(game.getImage())));
    }

    @GetMapping
    public String alunoPage(Model model) {
        List<Game> games = gameService.getAllActiveGames();
        model.addAttribute("games", games);
        model.addAttribute("imageMap", buildImageMap(games));
        return "aluno";
    }

    @GetMapping("/jogo/{id}")
    public String jogoPage(@PathVariable Long id, Model model) {
        Game game = gameService.getGameById(id).orElse(null);
        
        if (game == null) {
            model.addAttribute("erro", "Jogo não encontrado!");
            return "redirect:/embaralhar/aluno";
        }

        // Converter imagem para Base64 para exibir no HTML
        String imageBase64 = Base64.getEncoder().encodeToString(game.getImage());
        model.addAttribute("game", game);
        model.addAttribute("imageBase64", imageBase64);

        return "jogo";
    }

    @PostMapping("/enviar-resposta")
    public String enviarResposta(
            @RequestParam("gameId") Long gameId,
            @RequestParam("resposta") String resposta,
            Model model) {
        
        try {
            GameAttempt attempt = gameService.saveAttempt(gameId, resposta);
            
            Game game = gameService.getGameById(gameId).get();
            String imageBase64 = Base64.getEncoder().encodeToString(game.getImage());
            
            model.addAttribute("game", game);
            model.addAttribute("imageBase64", imageBase64);
            model.addAttribute("attempt", attempt);
            
            if (attempt.getCorrect()) {
                model.addAttribute("mensagem", "🎉 Parabéns! Você acertou!");
                model.addAttribute("sucesso", true);
            } else {
                model.addAttribute("mensagem", "❌ Errado! Tente novamente.");
                model.addAttribute("sucesso", false);
            }
            
            return "jogo";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao processar resposta: " + e.getMessage());
            return "jogo";
        }
    }
}