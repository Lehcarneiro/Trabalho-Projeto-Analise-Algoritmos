import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static String[] corretoraA;
    static String[] corretoraB;

    static ArrayList<String> melhor = new ArrayList<>();

    public static void buscar(int ultimaPosicaoA, int ultimaPosicaoB,
                              ArrayList<String> atual) {

        boolean encontrouProximo = false;

        for (int i = ultimaPosicaoA + 1; i < corretoraA.length; i++) {

            for (int j = ultimaPosicaoB + 1; j < corretoraB.length; j++) {

                if (corretoraA[i].equals(corretoraB[j])) {

                    encontrouProximo = true;

                    atual.add(corretoraA[i]);

                    buscar(i, j, atual);

                    atual.remove(atual.size() - 1);
                }
            }
        }

        if (!encontrouProximo && atual.size() > melhor.size()) {
            melhor = new ArrayList<>(atual);
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o tamanho do primeiro vetor");
        int M = scanner.nextInt();

        System.out.println("Digite o tamanho do segundo vetor");
        int N = scanner.nextInt();

        corretoraA = new String[M];
        corretoraB = new String[N];

        System.out.println("Digite a primeira coluna dos tickers de ações da B3");

        for (int i = 0; i < M; i++) {
            corretoraA[i] = scanner.next();
        }

        System.out.println("Digite a segunda coluna dos tickers de ações da B3");

        for (int i = 0; i < N; i++) {
            corretoraB[i] = scanner.next();
        }

        System.out.println("Primeira corretora:");

        for (int i = 0; i < M; i++) {
            System.out.println(corretoraA[i]);
        }

        System.out.println("Segunda corretora:");

        for (int i = 0; i < N; i++) {
            System.out.println(corretoraB[i]);
        }

        ArrayList<String> atual = new ArrayList<>();


        buscar(-1, -1, atual);

        System.out.println("Maior subsequência encontrada:");
        System.out.println();


        System.out.println(melhor.size());

        for (String ticker : melhor) {
            System.out.print(ticker + " ");
        }


    }
}