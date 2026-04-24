package br.com.projeto_letramento.projeto_letramento.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import br.com.projeto_letramento.projeto_letramento.model.PalavraModel;
import br.com.projeto_letramento.projeto_letramento.service.PalavraService;

@RestController
@RequestMapping("/palavras")
public class PalavraController {

    private final PalavraService service;

    public PalavraController(PalavraService service) {
        this.service = service;
    }

    @PostMapping("/{professorId}")
    public PalavraModel criar(
            @RequestParam("palavra") String palavra,
            @RequestParam("imagem") MultipartFile imagem,
            @PathVariable Integer professorId
    ) throws Exception {

        return service.salvarComImagem(palavra, imagem, professorId);
    }

    @GetMapping
    public List<PalavraModel> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public PalavraModel buscar(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}/{professorId}")
    public PalavraModel atualizar(
            @PathVariable Integer id,
            @RequestBody PalavraModel palavra,
            @PathVariable Integer professorId
    ) {
        return service.atualizar(id, palavra, professorId);
    }

    @DeleteMapping("/{id}/{professorId}")
    public void deletar(@PathVariable Integer id, @PathVariable Integer professorId) {
        service.deletar(id, professorId);
    }
}