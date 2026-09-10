import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String[] corretoraA;
        String[] corretoraB;
        int M;
        int N;
        int posicaoA = 0;
        int posicaoB = 0;
        int ultimaPosicaoA = -1;
        int ultimaPosicaoB = -1;

        System.out.println("Digite o tamanho do primeiro vetor");
        M = scanner.nextInt();


        System.out.println("Digite o tamanho do segundo vetor");
        N = scanner.nextInt();

        corretoraA = new String[M];
        corretoraB = new String[N];

        System.out.println("Digite a primeira coluna dos tickers de ações da B3");

        for(int i=0; i < M; i++){
            corretoraA[i] = scanner.next();
        }

        System.out.println("Digite a segunda coluna dos tickers de ações da B3");

        for(int i=0; i < M; i++){
            corretoraB[i] = scanner.next();
        }

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {

        if (corretoraA[i].equals(corretoraB[j])) {
                posicaoA = i;
                posicaoB = j;

            for (int proximoTickerA = ultimaPosicaoA + 1; proximoTickerA < M; proximoTickerA++) {

                for (int proximoTickerB = ultimaPosicaoB + 1; proximoTickerB < N; proximoTickerB++) {

                    if (corretoraA[proximoTickerA].equals(corretoraB[proximoTickerB])) {

                        System.out.println("Próximo: " + corretoraA[proximoTickerA]);
                        System.out.println("A: " + proximoTickerA);
                        System.out.println("B: " + proximoTickerB);
                            }
                        }
                    }
                }
            }
        }
    }
}