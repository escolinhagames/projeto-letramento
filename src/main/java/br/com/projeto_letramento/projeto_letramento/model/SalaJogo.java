package br.com.projeto_letramento.projeto_letramento.model;

import java.util.*;

/**
 * Representa uma sala de jogo com professor e alunos.
 */
public class SalaJogo {
    private String codigo;
    private String professor;
    private List<Integer> sorteados = new ArrayList<>();
    private List<Integer> cartelaProfessor = new ArrayList<>();
    private Map<String, Set<Integer>> cartelasAlunos = new HashMap<>();
    private Map<String, Set<Integer>> marcadosAlunos = new HashMap<>();
    private Map<String, Boolean> alunosBingo = new HashMap<>();
    private Random random = new Random();
    private boolean ativa = true;
    private static final int TAMANHO_CARTELA = 25;  // 5x5
    private static final int MAXIMO_NUMERO = 50;
    private static final int LIMITE_BINGO = 5; // Número de alunos que precisam completar a cartela para encerrar o jogo

    public SalaJogo(String codigo, String professor) {
        this.codigo = codigo;
        this.professor = professor;
        this.cartelaProfessor = gerarCartela();
    }

    /**
     * Gera uma nova cartela com números aleatórios (5x5 = 25 números)
     * Organiza os números em 5 colunas: 1-10, 11-20, 21-30, 31-40, 41-50
     */
    public List<Integer> gerarCartela() {
        List<Integer> cartela = new ArrayList<>();
        Set<Integer> usados = new HashSet<>();

        // 5 intervalos com 10 números cada (50 números totais)
        int[][] intervalos = {
            {1, 10},      // Coluna 1
            {11, 20},     // Coluna 2
            {21, 30},     // Coluna 3
            {31, 40},     // Coluna 4
            {41, 50}      // Coluna 5
        };

        // Distribui os 25 números da cartela entre os intervalos
        // Tenta colocar até 5 números de cada intervalo
        for (int[] intervalo : intervalos) {
            if (cartela.size() >= TAMANHO_CARTELA) break;

            int quantidade = Math.min(5, TAMANHO_CARTELA - cartela.size());
            Set<Integer> selecionados = new HashSet<>();

            // Lista todos os números disponíveis no intervalo
            List<Integer> disponiveis = new ArrayList<>();
            for (int i = intervalo[0]; i <= intervalo[1]; i++) {
                if (!usados.contains(i)) {
                    disponiveis.add(i);
                }
            }

            // Embaralha e pega os primeiros
            Collections.shuffle(disponiveis);
            for (int i = 0; i < Math.min(quantidade, disponiveis.size()); i++) {
                int numero = disponiveis.get(i);
                selecionados.add(numero);
                usados.add(numero);
            }

            cartela.addAll(selecionados);
        }

        // Se ainda faltam números, adiciona de qualquer lugar disponível
        List<Integer> todosDisponiveis = new ArrayList<>();
        for (int i = 1; i <= MAXIMO_NUMERO; i++) {
            if (!usados.contains(i)) {
                todosDisponiveis.add(i);
            }
        }

        Collections.shuffle(todosDisponiveis);
        for (int i = 0; i < Math.min(TAMANHO_CARTELA - cartela.size(), todosDisponiveis.size()); i++) {
            cartela.add(todosDisponiveis.get(i));
        }

        cartela.sort(Integer::compareTo);
        return cartela;
    }

    /**
     * Sorteia um novo número
     */
    public int sortearNumero() {
        if (sorteados.size() >= MAXIMO_NUMERO) {
            throw new IllegalStateException("Todos os números já foram sorteados!");
        }

        int numero;
        do {
            numero = random.nextInt(MAXIMO_NUMERO) + 1;
        } while (sorteados.contains(numero));

        sorteados.add(numero);
        return numero;
    }

    /**
     * Adiciona um aluno à sala
     */
    public void adicionarAluno(String nomeAluno) {
        if (!cartelasAlunos.containsKey(nomeAluno)) {
            Set<Integer> cartela = new HashSet<>(gerarCartela());
            cartelasAlunos.put(nomeAluno, cartela);
            marcadosAlunos.put(nomeAluno, new HashSet<>());
            alunosBingo.put(nomeAluno, false);
        }
    }

    /**
     * Marca um número para um aluno específico
     */
    public void marcarNumero(String nomeAluno, int numero) {
        if (cartelasAlunos.containsKey(nomeAluno)) {
            Set<Integer> cartela = cartelasAlunos.get(nomeAluno);
            Set<Integer> marcados = marcadosAlunos.get(nomeAluno);
            if (cartela.contains(numero) && marcados != null) {
                marcados.add(numero);
            }
        }
    }

    /**
     * Desmarca um número para um aluno específico
     */
    public void desmarcarNumero(String nomeAluno, int numero) {
        if (cartelasAlunos.containsKey(nomeAluno) && marcadosAlunos.containsKey(nomeAluno)) {
            marcadosAlunos.get(nomeAluno).remove(numero);
        }
    }

    /**
     * Verifica se um aluno completou o bingo (APENAS cartela cheia - todos os números marcados)
     * E registra no mapa de alunosBingo
     */
    public boolean verificarBingo(String nomeAluno) {
        if (!cartelasAlunos.containsKey(nomeAluno) || !marcadosAlunos.containsKey(nomeAluno)) {
            return false;
        }

        Set<Integer> cartelaAluno = cartelasAlunos.get(nomeAluno);
        Set<Integer> marcados = marcadosAlunos.get(nomeAluno);

        if (marcados.containsAll(cartelaAluno)) {
            alunosBingo.put(nomeAluno, true);
            return true;
        }

        return false;
    }

    /**
     * Verifica se o jogo deve ser encerrado (5 a 10 alunos completaram bingo)
     */
    public boolean verificarFimDoJogo() {
        long alunosComBingo = alunosBingo.values().stream().filter(b -> b).count();
        return alunosComBingo >= LIMITE_BINGO;
    }

    /**
     * Obtém o número de alunos que completaram bingo
     */
    public int getQuantidadeAlunosBingo() {
        return (int) alunosBingo.values().stream().filter(b -> b).count();
    }

    /**
     * Reseta a sala para um novo jogo
     */
    public void resetar() {
        sorteados.clear();
        alunosBingo.clear();
        cartelaProfessor = gerarCartela();
        
        // Gera novas cartelas para todos os alunos
        for (String aluno : cartelasAlunos.keySet()) {
            Set<Integer> cartela = new HashSet<>(gerarCartela());
            cartelasAlunos.put(aluno, cartela);
            marcadosAlunos.put(aluno, new HashSet<>());
            alunosBingo.put(aluno, false);
        }
    }

    // Getters
    public String getCodigo() { return codigo; }
    public String getProfessor() { return professor; }
    public List<Integer> getSorteados() { return new ArrayList<>(sorteados); }
    public List<Integer> getCartelaProfessor() { return new ArrayList<>(cartelaProfessor); }
    public Map<String, Set<Integer>> getCartelasAlunos() { return cartelasAlunos; }
    public Map<String, Boolean> getAlunosBingo() { return new HashMap<>(alunosBingo); }
    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
    public Set<String> getAlunos() { return cartelasAlunos.keySet(); }
    public int getTotalSorteados() { return sorteados.size(); }

    public List<Integer> getCartelaAluno(String nomeAluno) {
        if (!cartelasAlunos.containsKey(nomeAluno)) {
            return Collections.emptyList();
        }
        List<Integer> cartela = new ArrayList<>(cartelasAlunos.get(nomeAluno));
        Collections.sort(cartela);
        return cartela;
    }
}
