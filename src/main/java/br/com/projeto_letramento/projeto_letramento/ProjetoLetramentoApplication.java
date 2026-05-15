package br.com.projeto_letramento.projeto_letramento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling 
public class ProjetoLetramentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoLetramentoApplication.class, args);
	}

}
