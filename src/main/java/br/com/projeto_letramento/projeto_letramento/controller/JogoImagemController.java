package br.com.projeto_letramento.projeto_letramento.controller;

import br.com.projeto_letramento.projeto_letramento.model.JogoImagemModel;
import br.com.projeto_letramento.projeto_letramento.service.JogoImagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jogo-imagem")
@RequiredArgsConstructor
@CrossOrigin
public class JogoImagemController {

    private final JogoImagemService service;

    private Map<String, Object> toMap(JogoImagemModel j) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", j.getId());
        m.put("palavraCorreta", j.getPalavraCorreta());
        m.put("dificuldade", j.getDificuldade());
        m.put("dica", service.gerarDica(j.getPalavraCorreta(), j.getDificuldade()));
        m.put("primeiraLetra", String.valueOf(j.getPalavraCorreta().charAt(0)));
        m.put("imagem1", Base64.getEncoder().encodeToString(j.getImagem1()));
        m.put("imagem2", Base64.getEncoder().encodeToString(j.getImagem2()));
        m.put("imagem3", Base64.getEncoder().encodeToString(j.getImagem3()));
        m.put("criadoEm", j.getCriadoEm());
        return m;
    }

    @PostMapping("/professor/criar")
    public ResponseEntity<?> criar(
            @RequestParam("palavraCorreta") String palavraCorreta,
            @RequestParam("dificuldade") String dificuldade,
            @RequestParam("imagem1") MultipartFile img1,
            @RequestParam("imagem2") MultipartFile img2,
            @RequestParam("imagem3") MultipartFile img3,
            @RequestParam("professorId") Integer professorId
    ) throws IOException {
        JogoImagemModel jogo = service.criarSala(palavraCorreta, dificuldade, img1, img2, img3, professorId);
        return ResponseEntity.ok(Map.of("id", jogo.getId(), "mensagem", "Sala criada com sucesso"));
    }

    @PutMapping("/professor/editar/{id}")
    public ResponseEntity<?> editar(
            @PathVariable Long id,
            @RequestParam(value = "palavraCorreta", required = false) String palavraCorreta,
            @RequestParam(value = "dificuldade", required = false) String dificuldade,
            @RequestParam(value = "imagem1", required = false) MultipartFile img1,
            @RequestParam(value = "imagem2", required = false) MultipartFile img2,
            @RequestParam(value = "imagem3", required = false) MultipartFile img3
    ) throws IOException {
        JogoImagemModel jogo = service.atualizar(id, palavraCorreta, dificuldade, img1, img2, img3);
        return ResponseEntity.ok(toMap(jogo));
    }

    @GetMapping("/salas")
    public ResponseEntity<?> listar() {
        List<Map<String, Object>> salas = service.listarSalas()
                .stream().map(this::toMap).collect(Collectors.toList());
        return ResponseEntity.ok(salas);
    }

    @GetMapping("/professor/minhas-salas/{professorId}")
    public ResponseEntity<?> listarPorProfessor(@PathVariable Integer professorId) {
        List<Map<String, Object>> salas = service.listarPorProfessor(professorId)
                .stream().map(this::toMap).collect(Collectors.toList());
        return ResponseEntity.ok(salas);
    }

    @GetMapping("/salas/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(j -> ResponseEntity.ok(toMap(j)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/professor/deletar/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Sala deletada"));
    }

    @PatchMapping("/professor/desativar/{id}")
    public ResponseEntity<?> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Sala desativada"));
    }
}