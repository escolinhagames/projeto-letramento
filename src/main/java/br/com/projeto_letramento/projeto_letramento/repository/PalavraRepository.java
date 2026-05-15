package br.com.projeto_letramento.projeto_letramento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.projeto_letramento.projeto_letramento.model.PalavraModel;

import java.util.List;

public interface PalavraRepository extends JpaRepository<PalavraModel, Integer> {

    List<PalavraModel> findByExcluido(Integer excluido);

}