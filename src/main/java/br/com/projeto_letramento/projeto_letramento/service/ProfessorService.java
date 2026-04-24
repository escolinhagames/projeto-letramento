package br.com.projeto_letramento.projeto_letramento.service;

import java.util.List;
import org.springframework.stereotype.Service;

import br.com.projeto_letramento.projeto_letramento.model.ProfessorModel;
import br.com.projeto_letramento.projeto_letramento.repository.ProfessorRepository;

@Service
public class ProfessorService {

    private ProfessorRepository professorRepository;

    ProfessorService(ProfessorRepository professorRepository){
        this.professorRepository = professorRepository;
    }
    
    public ProfessorModel salvar(ProfessorModel professor){
        return professorRepository.save(professor);
    }

    public List<ProfessorModel> listar(){
        return professorRepository.findAll();
    }

    public ProfessorModel buscarPorId(Integer id){
        return professorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
    }

    public ProfessorModel login(String email, String senha){
        ProfessorModel professor = professorRepository.findByEmailAndSenha(email, senha);

        if(professor == null){
            throw new RuntimeException("Email ou senha inválidos");
        }

        return professor;
    }

    public ProfessorModel alterarSenha(Integer id, String novaSenha){
        ProfessorModel professor = buscarPorId(id);
        professor.setSenha(novaSenha);
        return professorRepository.save(professor);
    }
    public ProfessorModel atualizar(Integer id, ProfessorModel dados){
        ProfessorModel professor = buscarPorId(id);

        professor.setNome(dados.getNome());
        professor.setEmail(dados.getEmail());

        return professorRepository.save(professor);
    }

    public void deletar(Integer id){
        professorRepository.deleteById(id);
    }

    
}
