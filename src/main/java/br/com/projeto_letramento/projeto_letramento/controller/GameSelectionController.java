package br.com.projeto_letramento.projeto_letramento.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

@RestController
@RequestMapping("/api/jogos")
public class GameSelectionController {

    @GetMapping("/listar")
    public ResponseEntity<Map<String, Object>> listarJogos() {
        Map<String, Object> response = new HashMap<>();
        
        List<Map<String, String>> jogos = new ArrayList<>();
        
        Map<String, String> bingo = new HashMap<>();
        bingo.put("id", "1");
        bingo.put("nome", "Bingo");
        bingo.put("descricao", "Um clássico jogo de bingo para professores e alunos");
        bingo.put("urlInicio", "/bingo/aluno");
        bingo.put("rotas", "GET /bingo/aluno, GET /bingo/professor, POST /api/bingo/sala/criar");
        jogos.add(bingo);
        
        Map<String, String> jogo2 = new HashMap<>();
        jogo2.put("id", "2");
        jogo2.put("nome", "Embaralhar");
        jogo2.put("descricao", "Adivinhe a palavra pela imagem");
        jogo2.put("urlInicio", "/embaralhar");
        jogo2.put("rotas", "GET /embaralhar/**, POST /embaralhar/**");
        jogos.add(jogo2);
        
        Map<String, String> jogo3 = new HashMap<>();
        jogo3.put("id", "3");
        jogo3.put("nome", "Jogo 3");
        jogo3.put("descricao", "Descrição do terceiro jogo");
        jogo3.put("urlInicio", "/jogo3");
        jogo3.put("rotas", "Em desenvolvimento");
        jogos.add(jogo3);
        
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

    @GetMapping("/{idJogo}")
    public ResponseEntity<Map<String, String>> obterDetalhesJogo(@PathVariable String idJogo) {
        Map<String, String> jogo = new HashMap<>();
        
        switch (idJogo) {
            case "1":
                jogo.put("id", "1");
                jogo.put("nome", "Bingo");
                jogo.put("descricao", "Um clássico jogo de bingo para professores e alunos");
                jogo.put("urlInicio", "/bingo/aluno");
                jogo.put("instrucoes", "1. Professor cria uma sala\n2. Alunos entram com o código\n3. Professor sorteia números\n4. Alunos marcam suas cartelas");
                break;
            case "2":
                jogo.put("id", "2");
                jogo.put("nome", "Jogo 2");
                jogo.put("descricao", "Descrição do segundo jogo");
                jogo.put("urlInicio", "/jogo2");
                jogo.put("status", "Em desenvolvimento");
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