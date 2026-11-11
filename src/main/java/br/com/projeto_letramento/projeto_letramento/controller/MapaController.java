package br.com.projeto_letramento.projeto_letramento.controller;

import br.com.projeto_letramento.projeto_letramento.model.Estado;
import br.com.projeto_letramento.projeto_letramento.service.MapaService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jogo")
@CrossOrigin(origins = "*")
public class MapaController {

    private final MapaService gameService;

    public MapaController(@Qualifier("estadoGameService") MapaService gameService) {
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

    @GetMapping("/dificuldade-atual")
    public Map<String, String> getDificuldadeAtual() {
        return Map.of("dificuldade", gameService.getDificuldadeAtual());
    }

    @PostMapping("/dificuldade")
    public Map<String, String> atualizarDificuldade(@RequestBody Map<String, String> payload) {
        String dificuldade = payload == null ? null : payload.get("dificuldade");
        return Map.of("dificuldade", gameService.setDificuldade(dificuldade));
    }
}
