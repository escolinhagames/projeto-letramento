package br.com.projeto_letramento.projeto_letramento.service;

import br.com.projeto_letramento.projeto_letramento.model.Estado;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

@Service("estadoGameService")
public class MapaService {

    private static final List<Estado> ESTADOS = List.of(
        new Estado("AC", "Acre"),
        new Estado("AL", "Alagoas"),
        new Estado("AP", "Amapá"),
        new Estado("AM", "Amazonas"),
        new Estado("BA", "Bahia"),
        new Estado("CE", "Ceará"),
        new Estado("DF", "Distrito Federal"),
        new Estado("ES", "Espírito Santo"),
        new Estado("GO", "Goiás"),
        new Estado("MA", "Maranhão"),
        new Estado("MT", "Mato Grosso"),
        new Estado("MS", "Mato Grosso do Sul"),
        new Estado("MG", "Minas Gerais"),
        new Estado("PA", "Pará"),
        new Estado("PB", "Paraíba"),
        new Estado("PR", "Paraná"),
        new Estado("PE", "Pernambuco"),
        new Estado("PI", "Piauí"),
        new Estado("RJ", "Rio de Janeiro"),
        new Estado("RN", "Rio Grande do Norte"),
        new Estado("RS", "Rio Grande do Sul"),
        new Estado("RO", "Rondônia"),
        new Estado("RR", "Roraima"),
        new Estado("SC", "Santa Catarina"),
        new Estado("SP", "São Paulo"),
        new Estado("SE", "Sergipe"),
        new Estado("TO", "Tocantins")
    );

    private final AtomicReference<Estado> estadoAtual = new AtomicReference<>();

    public MapaService() {
        this.estadoAtual.set(sortearEstado(null));
    }

    public Estado getEstadoAtual() {
        return estadoAtual.get();
    }

    public Estado mudarEstado() {
        this.estadoAtual.updateAndGet(estado -> sortearEstado(estado));
        return estadoAtual.get();
    }

    private Estado sortearEstado(Estado estadoAtualAtual) {
        Estado proximoEstado = ESTADOS.get(ThreadLocalRandom.current().nextInt(ESTADOS.size()));

        while (estadoAtualAtual != null && proximoEstado.sigla().equals(estadoAtualAtual.sigla())) {
            proximoEstado = ESTADOS.get(ThreadLocalRandom.current().nextInt(ESTADOS.size()));
        }

        return proximoEstado;
    }
}
