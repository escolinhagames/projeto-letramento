package br.com.projeto_letramento.projeto_letramento.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller para servir as páginas estáticas do jogo Bingo.
 */
@Controller
public class BingoPaginasController {

    @GetMapping("/bingo/professor")
    public String professor() {
        return "forward:/professor.html";
    }

    @GetMapping("/bingo/aluno")
    public String aluno() {
        return "forward:/aluno.html";
    }

    @GetMapping("/bingo/jogo")
    public String jogo() {
        return "forward:/index.html";
    }
}
