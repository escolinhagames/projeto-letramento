package br.com.projeto_letramento.projeto_letramento.controller;

import java.util.List;

//import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.projeto_letramento.projeto_letramento.model.ProfessorModel;
import br.com.projeto_letramento.projeto_letramento.service.ProfessorService;

@RestController
@RequestMapping("/professores")
public class ProfessorController {
    // @PostMapping("/")
    // public void create(@RequestBody ProfessorModel ProfessorModel){
    // }
    
    ProfessorController(ProfessorService professorService){
        this.professorService = professorService;
    }
    private ProfessorService professorService;
    @PostMapping
    public ProfessorModel criar(@RequestBody ProfessorModel professor){
        return professorService.salvar(professor);
    }

    @GetMapping
    public List<ProfessorModel> listar(){
        return professorService.listar();
    }

    @GetMapping("/{id}")
    public ProfessorModel buscar(@PathVariable Integer id){
        return professorService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ProfessorModel atualizar(@PathVariable Integer id, @RequestBody ProfessorModel professor){
        return professorService.atualizar(id, professor);
    }

    @PostMapping("/login")
    public ProfessorModel login(@RequestBody ProfessorModel professor){
        return professorService.login(professor.getEmail(), professor.getSenha());
    }

    @PatchMapping("/{id}/senha")
    public ProfessorModel alterarSenha(@PathVariable Integer id, @RequestBody ProfessorModel professor){
        return professorService.alterarSenha(id, professor.getSenha());
    }

}
