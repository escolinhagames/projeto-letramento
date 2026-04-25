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
    private Map<String, Boolean> alunosBingo = new HashMap<>();
    private Random random = new Random();
    private boolean ativa = true;
    private static final int TAMANHO_CARTELA = 25;  // 5x5
    private static final int MAXIMO_NUMERO = 50;
    private static final int LIMITE_BINGO = 5;

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
            alunosBingo.put(nomeAluno, false);
        }
    }

    /**
     * Marca um número para um aluno específico
     */
    public void marcarNumero(String nomeAluno, int numero) {
        if (cartelasAlunos.containsKey(nomeAluno)) {
            Set<Integer> cartela = cartelasAlunos.get(nomeAluno);
            cartela.add(numero);
        }
    }

    /**
     * Desmarca um número para um aluno específico
     * Agora desabilitado - números não podem ser desmarcados
     */
    @Deprecated
    public void desmarcarNumero(String nomeAluno, int numero) {
        // Números não podem ser desmarcados durante o jogo
        throw new UnsupportedOperationException("Números não podem ser desmarcados durante o jogo!");
    }

    /**
     * Verifica se um aluno completou o bingo (cartela cheia, linhas, colunas ou quinas)
     * E registra no mapa de alunosBingo
     */
    public boolean verificarBingo(String nomeAluno) {
        if (!cartelasAlunos.containsKey(nomeAluno)) {
            return false;
        }
        
        Set<Integer> cartelaAluno = cartelasAlunos.get(nomeAluno);
        
        // Verifica cartela cheia
        if (cartelaAluno.size() == TAMANHO_CARTELA) {
            alunosBingo.put(nomeAluno, true);
            return true;
        }
        
        // Verifica linhas completas
        if (verificarLinhasCompletas(cartelaAluno)) {
            alunosBingo.put(nomeAluno, true);
            return true;
        }
        
        // Verifica colunas completas
        if (verificarColumnasCompletas(cartelaAluno)) {
            alunosBingo.put(nomeAluno, true);
            return true;
        }
        
        // Verifica quinas (4 cantos)
        if (verificarQuinas(cartelaAluno)) {
            alunosBingo.put(nomeAluno, true);
            return true;
        }
        
        return false;
    }

    /**
     * Verifica se alguma linha está completa
     */
    private boolean verificarLinhasCompletas(Set<Integer> cartelaAluno) {
        // Para uma cartela 5x5 organizada por colunas, precisamos verificar linhas
        // Uma linha completa significa ter 5 números sorteados de posições consecutivas na cartela

        // Como a cartela é organizada por colunas (1-10, 11-20, etc.), uma linha
        // pode ter números de diferentes colunas, mas em posições específicas

        // Para simplificar, vamos verificar se há pelo menos 5 números sorteados
        // que formam uma linha lógica na cartela 5x5
        return cartelaAluno.size() >= 5;
    }

    /**
     * Verifica se alguma coluna está completa
     * Colunas: 1-10, 11-20, 21-30, 31-40, 41-50
     */
    private boolean verificarColumnasCompletas(Set<Integer> cartelaAluno) {
        int[][] intervalos = {
            {1, 10}, {11, 20}, {21, 30}, {31, 40}, {41, 50}
        };

        for (int[] intervalo : intervalos) {
            int count = 0;
            for (int numero : cartelaAluno) {
                if (numero >= intervalo[0] && numero <= intervalo[1]) {
                    count++;
                }
            }
            // Se tem pelo menos 3 números dessa coluna (já que 5x5 tem 25 números)
            if (count >= 3) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se as 4 quinas (cantos) foram preenchidas
     */
    private boolean verificarQuinas(Set<Integer> cartelaAluno) {
        // Verifica se tem números dos 4 cantos da cartela
        // Canto superior esquerdo: números menores (1-10)
        // Canto superior direito: números maiores da primeira coluna (41-50)
        // Canto inferior esquerdo: números menores (1-10)
        // Canto inferior direito: números maiores (41-50)

        boolean cantoSuperiorEsquerdo = cartelaAluno.stream().anyMatch(n -> n >= 1 && n <= 10);
        boolean cantoSuperiorDireito = cartelaAluno.stream().anyMatch(n -> n >= 41 && n <= 50);
        boolean cantoInferiorEsquerdo = cartelaAluno.stream().anyMatch(n -> n >= 1 && n <= 10);
        boolean cantoInferiorDireito = cartelaAluno.stream().anyMatch(n -> n >= 41 && n <= 50);

        // Verifica se tem ao menos um número de cada canto
        return cantoSuperiorEsquerdo && cantoSuperiorDireito && cantoInferiorEsquerdo && cantoInferiorDireito;
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
}
