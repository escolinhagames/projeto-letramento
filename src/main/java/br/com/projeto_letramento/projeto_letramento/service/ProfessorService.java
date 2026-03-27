package br.com.projeto_letramento.projeto_letramento.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projeto_letramento.projeto_letramento.model.ProfessorModel;
import br.com.projeto_letramento.projeto_letramento.repository.ProfessorRepository;

@Service
public class ProfessorService {
    @Autowired
    private ProfessorRepository professorRepository;
    ProfessorService(){

    }
    //aqui
    public ProfessorModel salvar(ProfessorModel professor){
        return professorRepository.save(professor);
    }

    // LISTAR TODOS
    public List<ProfessorModel> listar(){
        return professorRepository.findAll();
    }

    // BUSCAR POR ID
    public ProfessorModel buscarPorId(Integer id){
        return professorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
    }

    // LOGIN
    public ProfessorModel login(String email, String senha){
        ProfessorModel professor = professorRepository.findByEmailAndSenha(email, senha);

        if(professor == null){
            throw new RuntimeException("Email ou senha inválidos");
        }

        return professor;
    }

    // ALTERAR SENHA
    public ProfessorModel alterarSenha(Integer id, String novaSenha){
        ProfessorModel professor = buscarPorId(id);
        professor.setSenha(novaSenha);
        return professorRepository.save(professor);
    }

    // ATUALIZAR DADOS
    public ProfessorModel atualizar(Integer id, ProfessorModel dados){
        ProfessorModel professor = buscarPorId(id);

        professor.setNome(dados.getNome());
        professor.setEmail(dados.getEmail());

        return professorRepository.save(professor);
    }

    // DELETAR
    public void deletar(Integer id){
        professorRepository.deleteById(id);
    }
    //terminaaqui
    ProfessorService(ProfessorRepository professorRepository){
        this.professorRepository = professorRepository;
    }

    
}
