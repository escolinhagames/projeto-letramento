package br.com.projeto_letramento.projeto_letramento.service;

import br.com.projeto_letramento.projeto_letramento.model.JogoMatematicaModel;
import br.com.projeto_letramento.projeto_letramento.repository.JogoMatematicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class JogoMatematicaService {

    private final JogoMatematicaRepository repository;
    private final Random random = new Random();

    public JogoMatematicaModel criarSala(String dificuldade, Integer professorId,
                                          Integer maxAcertos, Integer maxErros) {
        JogoMatematicaModel sala = new JogoMatematicaModel();
        sala.setDificuldade(dificuldade);
        sala.setProfessorId(professorId);
        sala.setMaxAcertos(maxAcertos != null ? maxAcertos : 5);
        sala.setMaxErros(maxErros != null ? maxErros : 3);
        sala.setAtiva(true);
        return repository.save(sala);
    }

    public List<JogoMatematicaModel> listarSalas() {
        return repository.findByAtivaTrue();
    }

    public List<JogoMatematicaModel> listarPorProfessor(Integer professorId) {
        return repository.findByProfessorIdAndAtivaTrue(professorId);
    }

    public void desativar(Long id) {
        repository.findById(id).ifPresent(sala -> {
            sala.setAtiva(false);
            repository.save(sala);
        });
    }

    public Map<String, Object> gerarQuestao(String dificuldade) {
        for (int tentativa = 0; tentativa < 30; tentativa++) {
            Map<String, Object> questao = tentarGerarQuestao(dificuldade);
            if (questao != null) return questao;
        }
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("expressao", "2 + 3");
        fallback.put("resultado", 5);
        fallback.put("alternativas", List.of("3", "4", "5", "6"));
        fallback.put("dificuldade", dificuldade);
        return fallback;
    }

    private Map<String, Object> tentarGerarQuestao(String dificuldade) {
        List<Integer> numeros = gerarNumeros(dificuldade);
        List<String> operadores = gerarOperadores(dificuldade, numeros.size() - 1);

        Integer resultado = calcularComPrecedencia(numeros, operadores);
        if (resultado == null || resultado <= 0) return null;

        String expressao = montarExpressao(numeros, operadores);
        List<String> alternativas = gerarAlternativas(resultado);

        Map<String, Object> questao = new HashMap<>();
        questao.put("expressao", expressao);
        questao.put("resultado", resultado);
        questao.put("alternativas", alternativas);
        questao.put("dificuldade", dificuldade);
        return questao;
    }

    private List<Integer> gerarNumeros(String dificuldade) {
        int qtd = "DIFICIL".equals(dificuldade) ? 3 + random.nextInt(2) : 2;
        List<Integer> nums = new ArrayList<>();
        for (int i = 0; i < qtd; i++) {
            nums.add(1 + random.nextInt(9));
        }
        return nums;
    }

    private List<String> gerarOperadores(String dificuldade, int quantidade) {
        List<String> ops = new ArrayList<>();

        if ("FACIL".equals(dificuldade)) {
            for (int i = 0; i < quantidade; i++) {
                ops.add(random.nextBoolean() ? "+" : "-");
            }
        } else if ("MEDIO".equals(dificuldade)) {
            ops.add(random.nextBoolean() ? "×" : "÷");
        } else {
            // DIFICIL: máximo 1 operação × ou ÷, resto + ou -
            for (int i = 0; i < quantidade; i++) {
                ops.add(random.nextBoolean() ? "+" : "-");
            }
            int pos = random.nextInt(quantidade);
            ops.set(pos, random.nextBoolean() ? "×" : "÷");
        }
        return ops;
    }

    private String montarExpressao(List<Integer> nums, List<String> ops) {
        StringBuilder sb = new StringBuilder(String.valueOf(nums.get(0)));
        for (int i = 0; i < ops.size(); i++) {
            sb.append(" ").append(ops.get(i)).append(" ").append(nums.get(i + 1));
        }
        return sb.toString();
    }

    private Integer calcularComPrecedencia(List<Integer> nums, List<String> ops) {
        List<Double> n = new ArrayList<>();
        for (int x : nums) n.add((double) x);
        List<String> o = new ArrayList<>(ops);

        // primeira passagem: × e ÷
        int i = 0;
        while (i < o.size()) {
            if ("×".equals(o.get(i)) || "÷".equals(o.get(i))) {
                double a = n.get(i);
                double b = n.get(i + 1);

                if ("÷".equals(o.get(i))) {
                    if (b == 0 || a % b != 0) return null;
                }

                double res = "×".equals(o.get(i)) ? a * b : a / b;
                n.set(i, res);
                n.remove(i + 1);
                o.remove(i);
            } else {
                i++;
            }
        }

        // segunda passagem: + e -
        double resultado = n.get(0);
        for (int j = 0; j < o.size(); j++) {
            double b = n.get(j + 1);
            if ("+".equals(o.get(j))) resultado += b;
            else resultado -= b;
        }

        if (resultado != Math.floor(resultado) || resultado <= 0) return null;
        return (int) resultado;
    }

    private List<String> gerarAlternativas(int correto) {
        Set<Integer> set = new LinkedHashSet<>();
        set.add(correto);
        int tentativas = 0;
        while (set.size() < 4 && tentativas < 50) {
            int variacao = correto + (random.nextInt(7) - 3);
            if (variacao > 0 && variacao != correto) set.add(variacao);
            tentativas++;
        }
        int extra = 1;
        while (set.size() < 4) {
            if (!set.contains(correto + extra)) set.add(correto + extra);
            extra++;
        }
        List<String> lista = new ArrayList<>();
        for (int v : set) lista.add(String.valueOf(v));
        Collections.shuffle(lista);
        return lista;
    }
}