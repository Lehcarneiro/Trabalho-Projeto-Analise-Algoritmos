import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = null;

        try {
            // Se foi passado o caminho de um arquivo como argumento, le do arquivo.
            // Caso contrario, le da entrada padrao (System.in).
            if (args.length > 0) {
                scanner = new Scanner(new File(args[0]));
            } else {
                scanner = new Scanner(System.in);
            }

            // Verifica se ha dados para leitura
            if (!scanner.hasNext()) {
                return;
            }

            // Leitura da quantidade de ativos de cada corretora
            // Tratamos possivel caractere de BOM (Byte Order Mark) no inicio do arquivo
            String primeiroToken = scanner.next();
            if (primeiroToken.startsWith("\uFEFF")) {
                primeiroToken = primeiroToken.substring(1);
            }
            int m = Integer.parseInt(primeiroToken);
            int n = scanner.nextInt();

            // Leitura dos ativos da Corretora A
            String[] corretoraA = new String[m];
            for (int i = 0; i < m; i++) {
                corretoraA[i] = scanner.next();
            }

            // Leitura dos ativos da Corretora B
            String[] corretoraB = new String[n];
            for (int j = 0; j < n; j++) {
                corretoraB[j] = scanner.next();
            }

            // Construcao da tabela de programacao dinamica (LCS)
            int[][] dp = construirTabelaLCS(corretoraA, corretoraB, m, n);

            // Quantidade maxima de ativos em comum (K)
            int k = dp[m][n];

            // Reconstrucao da sequencia de consenso
            String[] consenso = reconstruirSequencia(corretoraA, corretoraB, dp, m, n, k);

            // Saida dos resultados conforme a especificacao
            imprimirResultado(k, consenso);

        } catch (FileNotFoundException e) {
            System.err.println("Arquivo nao encontrado: " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    /**
     * Constroi a tabela de Programacao Dinamica para a Maior Subsequencia Comum (LCS).
     * dp[i][j] representa o tamanho da maior subsequencia comum considerando
     * os primeiros i elementos da corretora A e os primeiros j elementos da corretora B.
     */
    public static int[][] construirTabelaLCS(String[] a, String[] b, int m, int n) {
        int[][] dp = new int[m + 1][n + 1];

        // Os casos base sao implicitamente 0 no Java (dp[i][0] = 0 e dp[0][j] = 0).
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Se as acoes forem iguais, estendemos a subsequencia comum anterior
                if (a[i - 1].equals(b[j - 1])) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    // Caso contrario, pegamos o melhor resultado entre ignorar a[i-1] ou b[j-1]
                    if (dp[i - 1][j] >= dp[i][j - 1]) {
                        dp[i][j] = dp[i - 1][j];
                    } else {
                        dp[i][j] = dp[i][j - 1];
                    }
                }
            }
        }

        return dp;
    }

    /**
     * Reconstroi a sequencia de consenso a partir da tabela dp ja calculada.
     * Fazemos o caminho de volta (backtracking) da posicao (m, n) ate (0, 0).
     */
    public static String[] reconstruirSequencia(String[] a, String[] b, int[][] dp, int m, int n, int k) {
        String[] resultado = new String[k];
        int indiceResultado = k - 1; // Preenchemos de tras para frente

        int i = m;
        int j = n;

        while (i > 0 && j > 0) {
            // Se os elementos sao iguais, este ativo pertence a solucao otima
            if (a[i - 1].equals(b[j - 1])) {
                resultado[indiceResultado] = a[i - 1];
                indiceResultado--;
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                // O valor maximo veio de cima
                i--;
            } else {
                // O valor maximo veio da esquerda
                j--;
            }
        }

        return resultado;
    }

    /**
     * Imprime a saida no formato exato solicitado:
     * Primeira linha: K
     * Segunda linha: tickers separados por espaco (ou linha em branco se K == 0).
     */
    public static void imprimirResultado(int k, String[] consenso) {
        System.out.println(k);

        if (k == 0) {
            System.out.println();
        } else {
            for (int i = 0; i < k; i++) {
                System.out.print(consenso[i]);
                if (i < k - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
