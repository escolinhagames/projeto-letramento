package br.com.projeto_letramento.projeto_letramento.service;

import org.springframework.stereotype.Service;
import br.com.projeto_letramento.projeto_letramento.model.SalaJogo;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço responsável por gerenciar as salas de jogo do Bingo.
 */
@Service
public class BingoService {
    private Map<String, SalaJogo> salas = new ConcurrentHashMap<>();

    /**
     * Cria uma nova sala de jogo
     */
    public SalaJogo criarSala(String professor) {
        String codigo = gerarCodigo();
        SalaJogo sala = new SalaJogo(codigo, professor);
        salas.put(codigo, sala);
        return sala;
    }

    /**
     * Obtém uma sala pelo código
     */
    public SalaJogo obterSala(String codigo) {
        return salas.get(codigo);
    }

    /**
     * Verifica se uma sala existe
     */
    public boolean salaExiste(String codigo) {
        return salas.containsKey(codigo);
    }

    /**
     * Encerra uma sala
     */
    public void encerrarSala(String codigo) {
        SalaJogo sala = salas.get(codigo);
        if (sala != null) {
            sala.setAtiva(false);
        }
    }

    /**
     * Gera um código aleatório para a sala (4 dígitos)
     */
    private String gerarCodigo() {
        Random random = new Random();
        int codigo = 1000 + random.nextInt(9000);
        String codigoStr = String.valueOf(codigo);
        
        // Se já existe, tenta novamente
        if (salas.containsKey(codigoStr)) {
            return gerarCodigo();
        }
        
        return codigoStr;
    }

    public List<Map<String, Object>> listarSalasAtivas() {
    return salas.values().stream()
        .filter(SalaJogo::isAtiva)
        .map(sala -> {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("codigo", sala.getCodigo());
            map.put("professor", sala.getProfessor());
            return map;
        })
        .collect(java.util.stream.Collectors.toList());
    }
}
