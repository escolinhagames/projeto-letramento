package br.com.projeto_letramento.projeto_letramento.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import br.com.projeto_letramento.projeto_letramento.model.PalavraModel;
import br.com.projeto_letramento.projeto_letramento.model.ProfessorModel;
import br.com.projeto_letramento.projeto_letramento.repository.PalavraRepository;
import br.com.projeto_letramento.projeto_letramento.repository.ProfessorRepository;

@Service
public class PalavraService {

    private final PalavraRepository palavraRepository;
    private final ProfessorRepository professorRepository;

    public PalavraService(PalavraRepository palavraRepository, ProfessorRepository professorRepository) {
        this.palavraRepository = palavraRepository;
        this.professorRepository = professorRepository;
    }

    public PalavraModel salvarComImagem(String palavraTexto, MultipartFile imagem, Integer professorId) {

        ProfessorModel professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        try {
            // 🔥 CAMINHO ABSOLUTO (IMPORTANTE)
            String pasta = System.getProperty("user.dir") + "/uploads/";

            File diretorio = new File(pasta);
            if (!diretorio.exists()) {
                diretorio.mkdirs(); // 👈 melhor que mkdir
            }

            String nomeArquivo = System.currentTimeMillis() + "_" + imagem.getOriginalFilename();

            File arquivo = new File(pasta + nomeArquivo);

            imagem.transferTo(arquivo); // 🔥 pode dar erro aqui

            PalavraModel palavra = new PalavraModel();
            palavra.setPalavra(palavraTexto);
            palavra.setImagem(nomeArquivo);
            palavra.setProfessor(professor);
            palavra.setExcluido(0);

            return palavraRepository.save(palavra);

        } catch (IOException e) {
            e.printStackTrace(); // 👈 MOSTRA ERRO REAL NO CONSOLE
            throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage());
        }
    }

    public List<PalavraModel> listar() {
        return palavraRepository.findByExcluido(0);
    }

    public PalavraModel buscarPorId(Integer id) {
        return palavraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Palavra não encontrada"));
    }

    public PalavraModel atualizar(Integer id, PalavraModel dados, Integer professorId) {
        PalavraModel palavra = buscarPorId(id);

        ProfessorModel professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        palavra.setPalavra(dados.getPalavra());
        palavra.setImagem(dados.getImagem());
        palavra.setProfessor(professor);

        return palavraRepository.save(palavra);
    }

    public void deletar(Integer id, Integer professorId) {
        PalavraModel palavra = buscarPorId(id);

        ProfessorModel professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        palavra.setExcluido(1);
        palavra.setProfessor(professor);

        palavraRepository.save(palavra);
    }
}