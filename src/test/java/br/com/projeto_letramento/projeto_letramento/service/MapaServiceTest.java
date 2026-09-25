package br.com.projeto_letramento.projeto_letramento.service;

import br.com.projeto_letramento.projeto_letramento.model.Estado;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class MapaServiceTest {

    @Test
    void deveSorteiarEstadoInicial() {
        MapaService service = new MapaService();

        Estado estado = service.getEstadoAtual();

        assertNotNull(estado);
        assertFalse(estado.sigla().isBlank());
        assertFalse(estado.nome().isBlank());
    }

    @Test
    void deveGerarOutroEstadoQuandoMudar() {
        MapaService service = new MapaService();
        Estado estadoAtual = service.getEstadoAtual();

        Estado novoEstado = service.mudarEstado();

        assertNotNull(novoEstado);
        assertFalse(novoEstado.sigla().isBlank());
        assertFalse(novoEstado.nome().isBlank());
        assertNotEquals(estadoAtual.sigla(), novoEstado.sigla());
    }
}
