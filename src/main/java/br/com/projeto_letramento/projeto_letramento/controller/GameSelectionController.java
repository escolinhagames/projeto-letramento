package br.com.projeto_letramento.projeto_letramento.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

/**
 * Controller para seleção de jogos na tela inicial.
 * Permite que o professor/aluno escolha qual jogo jogar.
 */
@RestController
@RequestMapping("/api/jogos")
@CrossOrigin
public class GameSelectionController {

    /**
     * Retorna a lista de todos os jogos disponíveis
     * GET /api/jogos/listar
     */
    @GetMapping("/listar")
    public ResponseEntity<Map<String, Object>> listarJogos() {
        Map<String, Object> response = new HashMap<>();
        
        List<Map<String, String>> jogos = new ArrayList<>();
        
        // Jogo 1: Bingo
        Map<String, String> bingo = new HashMap<>();
        bingo.put("id", "1");
        bingo.put("nome", "Bingo");
        bingo.put("descricao", "Um clássico jogo de bingo para professores e alunos");
        bingo.put("urlInicio", "/bingo/jogo");
        bingo.put("rotas", "POST /api/bingo/sala/criar");
        jogos.add(bingo);
        
        // Jogo 2: Embaralhar
        Map<String, String> jogo2 = new HashMap<>();
        jogo2.put("id", "2");
        jogo2.put("nome", "Embaralhar");
        jogo2.put("descricao", "Adivinhe a palavra pela imagem");
        jogo2.put("urlInicio", "/embaralhar");
        jogo2.put("rotas", "GET /embaralhar/**, POST /embaralhar/**");
        jogos.add(jogo2);
        
        // Jogo 3: [Próximo Jogo]
        Map<String, String> jogo3 = new HashMap<>();
        jogo3.put("id", "3");
        jogo3.put("nome", "Jogo 3");
        jogo3.put("descricao", "Descrição do terceiro jogo");
        jogo3.put("urlInicio", "/jogo3");
        jogo3.put("rotas", "Em desenvolvimento");
        jogos.add(jogo3);
        
        // Jogo 4: [Próximo Jogo]
        Map<String, String> jogo4 = new HashMap<>();
        jogo4.put("id", "4");
        jogo4.put("nome", "Jogo 4");
        jogo4.put("descricao", "Descrição do quarto jogo");
        jogo4.put("urlInicio", "/jogo4");
        jogo4.put("rotas", "Em desenvolvimento");
        jogos.add(jogo4);
        
        response.put("total", jogos.size());
        response.put("jogos", jogos);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Retorna detalhes de um jogo específico
     * GET /api/jogos/{idJogo}
     */
    @GetMapping("/{idJogo}")
    public ResponseEntity<Map<String, String>> obterDetalhesJogo(@PathVariable String idJogo) {
        Map<String, String> jogo = new HashMap<>();
        
        switch (idJogo) {
            case "1":
                jogo.put("id", "1");
                jogo.put("nome", "Bingo");
                jogo.put("descricao", "Um clássico jogo de bingo para professores e alunos");
                jogo.put("urlInicio", "/bingo/jogo");
                jogo.put("instrucoes", "1. Professor cria uma sala\n2. Alunos entram com o código\n3. Professor sorteia números\n4. Alunos marcam suas cartelas");
                break;
            case "2":
                jogo.put("id", "2");
                jogo.put("nome", "Embaralhar");
                jogo.put("descricao", "Adivinhe a palavra pela imagem");
                jogo.put("urlInicio", "/embaralhar");
                jogo.put("instrucoes", "1. Professor cria jogos com palavras e imagens\n2. Alunos selecionam um jogo\n3. Formam a palavra com as letras embaralhadas\n4. Tentam acertar a palavra correta");
                break;
            case "3":
                jogo.put("id", "3");
                jogo.put("nome", "Jogo 3");
                jogo.put("descricao", "Descrição do terceiro jogo");
                jogo.put("urlInicio", "/jogo3");
                jogo.put("status", "Em desenvolvimento");
                break;
            case "4":
                jogo.put("id", "4");
                jogo.put("nome", "Jogo 4");
                jogo.put("descricao", "Descrição do quarto jogo");
                jogo.put("urlInicio", "/jogo4");
                jogo.put("status", "Em desenvolvimento");
                break;
            default:
                return ResponseEntity.badRequest().body(
                    Map.of("erro", "Jogo não encontrado")
                );
        }
        
        return ResponseEntity.ok(jogo);
    }
}
