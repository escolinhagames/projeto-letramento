package br.com.projeto_letramento.projeto_letramento.game;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jogo")
@CrossOrigin(origins = "*")
public class GameController {

    private final GameService gameService;

    public GameController(@Qualifier("estadoGameService") GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/estado-atual")
    public Estado getEstadoAtual() {
        return gameService.getEstadoAtual();
    }

    @PostMapping("/mudar-estado")
    public Estado mudarEstado() {
        return gameService.mudarEstado();
    }
}
