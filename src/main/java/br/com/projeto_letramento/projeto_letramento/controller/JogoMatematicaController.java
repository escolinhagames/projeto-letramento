package br.com.projeto_letramento.projeto_letramento.controller;

import br.com.projeto_letramento.projeto_letramento.model.JogoMatematicaModel;
import br.com.projeto_letramento.projeto_letramento.service.JogoMatematicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/matematica")
@RequiredArgsConstructor
public class JogoMatematicaController {

    private final JogoMatematicaService service;

    @PostMapping("/professor/criar")
    public ResponseEntity<Map<String, Object>> criar(
            @RequestParam String dificuldade,
            @RequestParam Integer professorId,
            @RequestParam(defaultValue = "5") Integer maxAcertos,
            @RequestParam(defaultValue = "3") Integer maxErros) {
        JogoMatematicaModel sala = service.criarSala(dificuldade, professorId,
                                                      maxAcertos, maxErros);
        Map<String, Object> resp = new HashMap<>();
        resp.put("id", sala.getId());
        resp.put("dificuldade", sala.getDificuldade());
        resp.put("maxAcertos", sala.getMaxAcertos());
        resp.put("maxErros", sala.getMaxErros());
        resp.put("mensagem", "Sala criada com sucesso!");
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/professor/minhas-salas/{professorId}")
    public ResponseEntity<List<Map<String, Object>>> listarPorProfessor(
            @PathVariable Integer professorId) {
        return ResponseEntity.ok(
            service.listarPorProfessor(professorId).stream()
                .map(this::toMap).collect(Collectors.toList())
        );
    }

    @PostMapping("/professor/desativar/{id}")
    public ResponseEntity<Map<String, String>> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Sala desativada"));
    }

    @GetMapping("/aluno/salas")
    public ResponseEntity<List<Map<String, Object>>> listarSalas() {
        return ResponseEntity.ok(
            service.listarSalas().stream()
                .map(this::toMap).collect(Collectors.toList())
        );
    }

    @GetMapping("/aluno/questao/{salaId}")
    public ResponseEntity<Map<String, Object>> gerarQuestao(@PathVariable Long salaId) {
        return service.listarSalas().stream()
            .filter(s -> s.getId().equals(salaId))
            .findFirst()
            .map(sala -> ResponseEntity.ok(service.gerarQuestao(sala.getDificuldade())))
            .orElse(ResponseEntity.notFound().build());
    }

    private Map<String, Object> toMap(JogoMatematicaModel s) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", s.getId());
        m.put("dificuldade", s.getDificuldade());
        m.put("maxAcertos", s.getMaxAcertos());
        m.put("maxErros", s.getMaxErros());
        m.put("criadoEm", s.getCriadoEm() != null ? s.getCriadoEm().toString() : "");
        return m;
    }
}