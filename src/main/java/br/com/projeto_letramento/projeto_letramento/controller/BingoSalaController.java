package br.com.projeto_letramento.projeto_letramento.controller;

import br.com.projeto_letramento.projeto_letramento.model.SalaJogo;
import br.com.projeto_letramento.projeto_letramento.service.BingoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.*;

@RestController
@RequestMapping("/api/bingo/sala")
public class BingoSalaController {

    private final BingoService bingoService;

    public BingoSalaController(BingoService bingoService) {
        this.bingoService = bingoService;
    }

    @PostMapping("/criar")
    public ResponseEntity<Map<String, String>> criarSala(@RequestParam String professor) {
        try {
            SalaJogo sala = bingoService.criarSala(professor);
            Map<String, String> response = new HashMap<>();
            response.put("codigo", sala.getCodigo());
            response.put("professor", sala.getProfessor());
            response.put("status", "Sala criada com sucesso!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("erro", "Erro ao criar sala")
            );
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Map<String, Object>> obterSala(@PathVariable String codigo) {
        SalaJogo sala = bingoService.obterSala(codigo);
        
        if (sala == null || !sala.isAtiva()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("erro", "Sala não encontrada ou inativa")
            );
        }

        Map<String, Object> response = new HashMap<>();
        response.put("codigo", sala.getCodigo());
        response.put("professor", sala.getProfessor());
        response.put("sorteados", sala.getSorteados());
        response.put("ultimoNumero", sala.getTotalSorteados() > 0 ? 
            sala.getSorteados().stream().max(Integer::compareTo).orElse(0) : 0);
        response.put("totalSorteados", sala.getTotalSorteados());
        response.put("alunos", sala.getAlunos());
        response.put("totalAlunos", sala.getAlunos().size());
        response.put("jogoEncerrado", sala.verificarFimDoJogo());
        response.put("quantidadeAlunosBingo", sala.getQuantidadeAlunosBingo());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{codigo}/sortear")
    public ResponseEntity<Map<String, Object>> sortear(@PathVariable String codigo) {
        SalaJogo sala = bingoService.obterSala(codigo);
        
        if (sala == null || !sala.isAtiva()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("erro", "Sala não encontrada")
            );
        }

        try {
            int numero = sala.sortearNumero();
            Map<String, Object> response = new HashMap<>();
            response.put("numero", numero);
            response.put("totalSorteados", sala.getTotalSorteados());
            response.put("sorteados", sala.getSorteados());
            response.put("jogoEncerrado", sala.verificarFimDoJogo());
            response.put("quantidadeAlunosBingo", sala.getQuantidadeAlunosBingo());
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("erro", "Não há mais números para sortear")
            );
        }
    }

    @PostMapping("/{codigo}/aluno/entrar")
    public ResponseEntity<Map<String, Object>> entrarSala(
        @PathVariable String codigo,
        @RequestParam String nome) {
        
        SalaJogo sala = bingoService.obterSala(codigo);
        
        if (sala == null || !sala.isAtiva()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("erro", "Sala não encontrada")
            );
        }

        sala.adicionarAluno(nome);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Aluno entrou na sala com sucesso!");
        response.put("nome", nome);
        response.put("cartela", sala.getCartelaAluno(nome));
        response.put("codigo", codigo);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{codigo}/aluno/{nome}/marcar")
    public ResponseEntity<Map<String, String>> marcarNumero(
        @PathVariable String codigo,
        @PathVariable String nome,
        @RequestParam int numero) {
        
        SalaJogo sala = bingoService.obterSala(codigo);
        
        if (sala == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("erro", "Sala não encontrada")
            );
        }

        sala.marcarNumero(nome, numero);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "Número marcado");
        response.put("numero", String.valueOf(numero));
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{codigo}/aluno/{nome}/desmarcar")
    public ResponseEntity<Map<String, String>> desmarcarNumero(
        @PathVariable String codigo,
        @PathVariable String nome,
        @RequestParam int numero) {
        
        SalaJogo sala = bingoService.obterSala(codigo);
        
        if (sala == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("erro", "Sala não encontrada")
            );
        }

        sala.desmarcarNumero(nome, numero);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "Número desmarcado");
        response.put("numero", String.valueOf(numero));
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{codigo}/aluno/{nome}/bingo")
    public ResponseEntity<Map<String, Object>> verificarBingo(
        @PathVariable String codigo,
        @PathVariable String nome) {
        
        SalaJogo sala = bingoService.obterSala(codigo);
        
        if (sala == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("erro", "Sala não encontrada")
            );
        }

        boolean bingo = sala.verificarBingo(nome);
        
        Map<String, Object> response = new HashMap<>();
        response.put("nome", nome);
        response.put("bingo", bingo);
        response.put("jogoEncerrado", sala.verificarFimDoJogo());
        response.put("quantidadeAlunosBingo", sala.getQuantidadeAlunosBingo());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{codigo}/reset")
    public ResponseEntity<Map<String, String>> resetarSala(@PathVariable String codigo) {
        SalaJogo sala = bingoService.obterSala(codigo);
        
        if (sala == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("erro", "Sala não encontrada")
            );
        }

        sala.resetar();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "Sala resetada com sucesso!");
        response.put("codigo", codigo);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{codigo}/encerrar")
    public ResponseEntity<Map<String, String>> encerrarSala(@PathVariable String codigo) {
        bingoService.encerrarSala(codigo);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "Sala encerrada!");
        response.put("codigo", codigo);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/ativas")
    public ResponseEntity<List<Map<String, Object>>> listarSalasAtivas() {
        // adicione esse método ao BingoService também
        List<Map<String, Object>> salas = bingoService.listarSalasAtivas();
        return ResponseEntity.ok(salas);
    }
}