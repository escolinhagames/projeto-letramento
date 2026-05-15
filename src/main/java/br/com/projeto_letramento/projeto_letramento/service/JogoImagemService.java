package br.com.projeto_letramento.projeto_letramento.service;

import br.com.projeto_letramento.projeto_letramento.model.JogoImagemModel;
import br.com.projeto_letramento.projeto_letramento.repository.JogoImagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JogoImagemService {

    private final JogoImagemRepository repository;

    public JogoImagemModel criarSala(
            String palavraCorreta,
            String dificuldade,
            MultipartFile img1,
            MultipartFile img2,
            MultipartFile img3,
            Integer professorId) throws IOException {

        JogoImagemModel jogo = new JogoImagemModel();
        jogo.setPalavraCorreta(palavraCorreta.toUpperCase().trim());
        jogo.setDificuldade(dificuldade.toUpperCase());
        jogo.setImagem1(img1.getBytes());
        jogo.setImagem2(img2.getBytes());
        jogo.setImagem3(img3.getBytes());
        jogo.setProfessorId(professorId);

        return repository.save(jogo);
    }

    public JogoImagemModel atualizar(
            Long id,
            String palavraCorreta,
            String dificuldade,
            MultipartFile img1,
            MultipartFile img2,
            MultipartFile img3) throws IOException {

        JogoImagemModel jogo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala não encontrada"));

        if (palavraCorreta != null && !palavraCorreta.isBlank())
            jogo.setPalavraCorreta(palavraCorreta.toUpperCase().trim());

        if (dificuldade != null && !dificuldade.isBlank())
            jogo.setDificuldade(dificuldade.toUpperCase());

        if (img1 != null && !img1.isEmpty()) jogo.setImagem1(img1.getBytes());
        if (img2 != null && !img2.isEmpty()) jogo.setImagem2(img2.getBytes());
        if (img3 != null && !img3.isEmpty()) jogo.setImagem3(img3.getBytes());

        return repository.save(jogo);
    }

    public List<JogoImagemModel> listarSalas() {
        return repository.findByAtivaTrue();
    }

    public List<JogoImagemModel> listarPorProfessor(Integer professorId) {
        return repository.findByProfessorIdAndAtivaTrue(professorId);
    }

    public Optional<JogoImagemModel> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void desativar(Long id) {
        repository.findById(id).ifPresent(j -> {
            j.setAtiva(false);
            repository.save(j);
        });
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    // 🔥 Deleta salas com mais de 3 dias — roda todo dia à meia-noite
    @Scheduled(cron = "0 0 0 * * *")
    public void deletarSalasAntigas() {
        LocalDateTime limite = LocalDateTime.now().minusDays(3);
        List<JogoImagemModel> antigas = repository.findByCriadoEmBefore(limite);
        repository.deleteAll(antigas);
    }

    // 🔥 Gera dica de letras conforme dificuldade
    public String gerarDica(String palavra, String dificuldade) {
        if (palavra == null || palavra.isEmpty()) return "";

        String p = palavra.toUpperCase();

        if ("FACIL".equals(dificuldade)) {
            return String.valueOf(p.charAt(0));
        }

        // Sempre inclui a primeira letra
        List<Character> letras = new ArrayList<>();
        letras.add(p.charAt(0));

        // Índices disponíveis (excluindo o primeiro)
        List<Integer> indices = new ArrayList<>();
        for (int i = 1; i < p.length(); i++) indices.add(i);
        Collections.shuffle(indices);

        // Pega 2 índices aleatórios
        int qtd = Math.min(2, indices.size());
        List<Integer> escolhidos = new ArrayList<>(indices.subList(0, qtd));
        escolhidos.add(0); // adiciona índice 0 (primeira letra)

        if ("MEDIO".equals(dificuldade)) {
            // Ordena para manter sequência da palavra
            Collections.sort(escolhidos);
            StringBuilder sb = new StringBuilder();
            for (int i : escolhidos) sb.append(p.charAt(i));
            return sb.toString();
        }

        if ("DIFICIL".equals(dificuldade)) {
            // Embaralha a ordem das letras escolhidas
            List<Character> chars = new ArrayList<>();
            for (int i : escolhidos) chars.add(p.charAt(i));
            Collections.shuffle(chars);
            StringBuilder sb = new StringBuilder();
            for (char c : chars) sb.append(c);
            return sb.toString();
        }

        return String.valueOf(p.charAt(0));
    }
}