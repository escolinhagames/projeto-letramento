package br.com.projeto_letramento.projeto_letramento.game;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class GameServiceTest {

    @Test
    void deveSorteiarEstadoInicial() {
        GameService service = new GameService();

        Estado estado = service.getEstadoAtual();

        assertNotNull(estado);
        assertFalse(estado.sigla().isBlank());
        assertFalse(estado.nome().isBlank());
    }

    @Test
    void deveGerarOutroEstadoQuandoMudar() {
        GameService service = new GameService();
        Estado estadoAtual = service.getEstadoAtual();

        Estado novoEstado = service.mudarEstado();

        assertNotNull(novoEstado);
        assertFalse(novoEstado.sigla().isBlank());
        assertFalse(novoEstado.nome().isBlank());
        assertNotEquals(estadoAtual.sigla(), novoEstado.sigla());
    }
}
