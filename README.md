# Trabalho Prático 1 - Alinhamento Estratégico de Carteiras
**Disciplina:** Projeto e Análise de Algoritmos  
**Docente:** Prof. Douglas Castilho  
**Instituição:** Instituto Federal do Sul de Minas Gerais (IFSULDEMINAS) - Campus Poços de Caldas  
**Curso:** Engenharia de Computação (6º Período)  

---

## 1. Descrição do Problema

No mercado financeiro, diferentes corretoras e casas de análise (*research houses*) publicam periodicamente suas recomendações de alocação de ativos em listas ordenadas. Cada lista reflete uma ordem estrita de prioridade e preferência de compra para as ações (tickers da B3, tais como PETR4, VALE3, ITUB4, etc.).

Uma gestora de investimentos deseja estabelecer uma estratégia denominada **"Núcleo de Consenso Ordenado"** a partir dos relatórios de duas corretoras distintas (Corretora A e Corretora B). O objetivo principal é encontrar a **maior sequência de ações em comum que preserva rigorosamente a ordem relativa de recomendação em ambas as casas**.

Isso significa que, se a Corretora A recomenda o ativo $X$ antes do ativo $Y$, e a Corretora B também recomenda $X$ antes de $Y$, o par $(X, Y)$ é compatível e pode figurar na estratégia de consenso, mesmo que existam outros ativos intercalados entre eles em cada lista.

### Especificação de Entrada e Saída
- **Entrada:**
  - Pode ser fornecida via arquivo de texto ou pela entrada padrão (`stdin`).
  - **Linha 1:** Dois números inteiros $M$ e $N$ ($1 \le M, N \le 1000$), indicando respectivamente a quantidade de ações da Corretora A e da Corretora B.
  - **Linha 2:** $M$ códigos de ativos (strings em maiúsculas sem espaços), separados por espaço.
  - **Linha 3:** $N$ códigos de ativos, separados por espaço.
- **Saída:**
  - **Linha 1:** Um inteiro $K$, correspondente à quantidade máxima de ativos pertencentes à sequência de consenso.
  - **Linha 2:** Os $K$ códigos de ativos separados por espaço, na ordem em que aparecem concomitantemente em ambas as listas. Caso não haja ações em comum ($K = 0$), deve-se imprimir `0` na primeira linha e uma linha em branco na segunda. Se houver mais de uma sequência máxima de tamanho $K$, qualquer uma delas é aceita.

---

## 2. Modelagem da Solução e Referencial Teórico (CLRS)

Ao analisar as propriedades do problema, observa-se que ele é uma instância direta do problema clássico da **Maior Subsequência Comum (Longest Common Subsequence - LCS)**, amplamente documentado na literatura de algoritmos (Cormen et al. - *Introduction to Algorithms*, CLRS).

### Por que o problema é uma LCS?
Em ciência da computação, uma *subsequência* de uma sequência dada é obtida eliminando-se zero ou mais elementos, sem alterar a ordem dos elementos restantes. Ao contrário de uma *substring* (que exige elementos estritamente contíguos), na subsequência os elementos podem estar separados. Como as corretoras podem ter ações intercaladas diferentes, mas a ordem relativa de recomendação deve ser mantida, o consenso procurado é precisamente a subsequência comum de comprimento máximo entre a lista da Corretora A e a lista da Corretora B.

### Por que utilizar Programação Dinâmica?
Uma abordagem puramente ingênua por força bruta (testando todas as possíveis subsequências de uma lista contra a outra) teria complexidade exponencial da ordem de $O(2^M)$ ou $O(2^N)$, tornando a execução inviável mesmo para valores pequenos de $M$ e $N$.

O problema de LCS exibe as duas características fundamentais que justificam o uso de **Programação Dinâmica (Dynamic Programming)**, conforme formalizado no CLRS:

1. **Subestrutura Ótima (Optimal Substructure):**  
   Sejam $A = \langle a_1, a_2, \dots, a_m \rangle$ e $B = \langle b_1, b_2, \dots, b_n \rangle$ as duas listas de ativos, e seja $Z = \langle z_1, z_2, \dots, z_k \rangle$ uma LCS qualquer de $A$ e $B$:
   - Se $a_m = b_n$, então $z_k = a_m = b_n$ e o prefixo $Z_{k-1}$ é uma LCS de $A_{m-1}$ e $B_{n-1}$.
   - Se $a_m \ne b_n$, então se $z_k \ne a_m$, temos que $Z$ é uma LCS de $A_{m-1}$ e $B$.
   - Se $a_m \ne b_n$, então se $z_k \ne b_n$, temos que $Z$ é uma LCS de $A$ e $B_{n-1}$.
   Portanto, a solução ótima do problema global contém em seu interior as soluções ótimas de subproblemas menores.

2. **Subproblemas Sobrepostos (Overlapping Subproblems):**  
   Ao tentar resolver recursivamente, os mesmos subproblemas (LCS de prefixos $A[1..i]$ e $B[1..j]$) seriam recalculados inúmeras vezes. Com a programação dinâmica, resolvemos cada subproblema uma única vez e armazenamos o resultado em uma tabela bidimensional (*bottom-up* / tabulação), consultando os valores pré-calculados em tempo $O(1)$.

---

## 3. Explicação Detalhada do Algoritmo

### Significado de `dp[i][j]`
Definimos uma matriz bidimensional `dp` de dimensões $(M + 1) \times (N + 1)$, onde cada posição:
$$\text{dp}[i][j]$$
armazena o **comprimento da maior subsequência comum** considerando apenas o prefixo com os primeiros $i$ ativos da Corretora A ($A[0 \dots i-1]$) e os primeiros $j$ ativos da Corretora B ($B[0 \dots j-1]$).

### Relação de Recorrência
- **Casos Base:**
  - $\text{dp}[i][0] = 0$, para todo $0 \le i \le M$ (se a Corretora B não tem ativos considerados, a LCS tem tamanho 0).
  - $\text{dp}[0][j] = 0$, para todo $0 \le j \le N$ (se a Corretora A não tem ativos considerados, a LCS tem tamanho 0).
- **Passo Indutivo (para $1 \le i \le M$ e $1 \le j \le N$):**
  - Se os ativos atuais forem idênticos ($A[i-1] == B[j-1]$):
    $$\text{dp}[i][j] = \text{dp}[i-1][j-1] + 1$$
  - Se os ativos forem diferentes ($A[i-1] \ne B[j-1]$):
    $$\text{dp}[i][j] = \max(\text{dp}[i-1][j], \, \text{dp}[i][j-1])$$

### Preenchimento da Tabela
A tabela é preenchida iterativamente por meio de dois laços `for` aninhados: a variável $i$ varia de 1 até $M$, e a variável $j$ varia de 1 até $N$. Ao final do preenchimento, o valor da quantidade máxima de ativos $K$ estará diretamente na posição $\text{dp}[M][N]$.

### Reconstrução da Sequência (Backtracking)
Para recuperar os códigos das ações que compõem o consenso, partimos da posição final $(M, N)$ e fazemos o caminho de volta até alcançar a borda da matriz ($i = 0$ ou $j = 0$):
1. Se $A[i-1]$ for igual a $B[j-1]$, significa que esse ativo fez parte da subsequência comum. Adicionamos o ativo ao resultado e nos movemos na diagonal: $i \leftarrow i - 1$ e $j \leftarrow j - 1$.
2. Se forem diferentes, verificamos de onde veio o valor máximo:
   - Se $\text{dp}[i-1][j] > \text{dp}[i][j-1]$, movemos para cima ($i \leftarrow i - 1$).
   - Caso contrário, movemos para a esquerda ($j \leftarrow j - 1$).

Como o caminho de volta encontra os elementos da direita para a esquerda (do fim para o início), alocamos previamente um vetor de tamanho $K$ e o preenchemos a partir do índice $K-1$ descendo até 0. Assim, a ordem cronológica original é preservada sem necessidade de chamadas recursivas adicionais ou inversões de lista.

---

## 4. Análise de Complexidade Assintótica

A análise é realizada formalmente em função de $M$ (número de ativos da Corretora A) e $N$ (número de ativos da Corretora B), conforme solicitado no enunciado.

### 4.1. Complexidade de Tempo
A execução do programa é composta pelas seguintes etapas:
1. **Leitura da Entrada:**  
   São lidos $M$ tokens para a Corretora A e $N$ tokens para a Corretora B. Utilizando `Scanner`, essa etapa consome tempo linear $\Theta(M + N)$.
2. **Construção da Tabela DP:**  
   A matriz possui $(M + 1) \times (N + 1)$ posições. Os laços percorrem cada par $(i, j)$ exatamente uma vez. Em cada iteração, são realizadas apenas comparações de strings e acessos diretos à matriz, operações executadas em tempo $O(1)$. Logo, o tempo gasto é $\Theta(M \times N)$.
3. **Reconstrução da Resposta:**  
   O laço de *backtracking* inicia em $(M, N)$ e, a cada iteração, decrementa $i$, $j$ ou ambos simultaneamente. No pior caso, o laço executa no máximo $M + N$ iterações, cada uma com custo $O(1)$. Portanto, a reconstrução é $O(M + N)$.
4. **Impressão da Saída:**  
   Imprime o valor $K$ e os $K$ ativos encontrados, onde $K \le \min(M, N)$. Custo $O(K) = O(\min(M, N))$.

**Complexidade de Tempo Total:**
$$T(M, N) = O(M + N) + O(M \times N) + O(M + N) + O(K) = O(M \times N)$$

Para os limites do problema ($M, N \le 1000$), $M \times N \le 10^6$ iterações. Em computadores modernos e na JVM, $10^6$ operações simples de inteiros levam menos de 20 milissegundos, atendendo com extrema folga a qualquer limite de tempo estipulado.

### 4.2. Complexidade de Espaço
O consumo de memória adicional decorre de:
1. **Armazenamento das Listas de Entrada:**  
   Dois vetores de strings de tamanhos $M$ e $N$, ocupando espaço $O(M + N)$.
2. **Matriz de Programação Dinâmica:**  
   Uma matriz de inteiros primitivos de tamanho $(M + 1) \times (N + 1)$. Em Java, cada inteiro ocupa 4 bytes. Para o caso máximo ($M = 1000, N = 1000$), a matriz possui cerca de $1.002.001$ inteiros, consumindo aproximadamente 4 MB de memória RAM, o que é desprezível para a JVM. A complexidade de espaço é $O(M \times N)$.
3. **Vetor de Resposta:**  
   Um vetor de strings de tamanho $K \le \min(M, N)$, ocupando espaço $O(K)$.

**Complexidade de Espaço Total:**
$$S(M, N) = O(M \times N)$$

*Nota de Decisão de Projeto:* Embora existam variações para calcular apenas o tamanho $K$ usando apenas duas linhas da matriz ($O(\min(M, N))$ de espaço), a reconstrução da sequência completa exigiria técnicas mais sofisticadas (como o algoritmo de divisão e conquista de Hirschberg). Optou-se pela matriz completa $O(M \times N)$ porque ela mantém o código simples, transparente, fácil de depurar e perfeitamente compatível com os limites de memória ($1000 \times 1000 \approx 4\text{ MB}$).

---

## 5. Instruções de Compilação e Execução

O código-fonte foi desenvolvido em Java padrão, sem uso de bibliotecas externas, garantindo total portabilidade.

### Compilação
A partir do diretório onde está o arquivo `App.java` (ou a partir da pasta `Trabalho-1`):

```bash
# Se estiver dentro de Trabalho-1:
javac -d bin src/App.java

# Ou se compilar diretamente no diretório do arquivo:
javac App.java
```

### Execução
O programa aceita a entrada tanto por redirecionamento via `stdin` quanto por argumento de linha de comando apontando para o arquivo de texto:

```bash
# Opção 1: Passando o arquivo de entrada como argumento
java -cp bin App testes/exemplo1.txt

# Opção 2: Passando pela entrada padrão (stdin)
java -cp bin App < testes/exemplo1.txt
```

---

## 6. Casos de Teste Executados

Foram realizados testes exaustivos cobrindo todos os requisitos e casos de borda:

| Teste | Descrição | Entrada | Saída Obtida | Status |
|---|---|---|---|---|
| **Exemplo 1 (PDF)** | Caso geral com 3 ativos em comum | $M=5, N=6$ | `3` <br> `PETR4 ITUB4 ABEV3` | Sucesso |
| **Exemplo 2 (PDF)** | Nenhum ativo em comum ($K=0$) | $M=4, N=4$ | `0` <br> *(linha em branco)* | Sucesso |
| **Exemplo 3 (PDF)** | Múltiplas soluções máximas | $M=6, N=6$ | `3` <br> `VALE3 BBDC4 WEGE3` | Sucesso |
| **Teste 1** | Conjuntos disjuntos | $M=3, N=3$ | `0` <br> *(linha em branco)* | Sucesso |
| **Teste 2** | Apenas um ativo em comum | $M=4, N=3$ | `1` <br> `ITUB4` | Sucesso |
| **Teste 3** | Listas exatamente iguais | $M=5, N=5$ | `5` <br> *(lista completa)* | Sucesso |
| **Teste 4** | Listas com tamanhos díspares | $M=2, N=7$ | `2` <br> `PETR4 WEGE3` | Sucesso |
| **Teste 5** | Vários ativos em comum intercalados | $M=8, N=8$ | `7` <br> *(7 ativos em comum)* | Sucesso |
| **Teste 6** | Múltiplos empates de subsequência | $M=4, N=4$ | `2` <br> `ACAO2 ACAO4` | Sucesso |
| **Teste 7** | Ordem dos ativos invertida | $M=4, N=4$ | `1` <br> `BBDC4` | Sucesso |
| **Teste 8 (Limite)** | Caso extremo no limite máximo | $M=1000, N=1000$ | `500` <br> *(execução em ~150ms)* | Sucesso |

---

## 7. Descrição do Uso de Inteligência Artificial

Em conformidade com as orientações do trabalho prático e prezando pela integridade acadêmica, relata-se o uso de Inteligência Artificial durante o desenvolvimento deste trabalho:

- **Ferramenta utilizada:** Agente de IA Antigravity (Google DeepMind).
- **Finalidade e Escopo de Utilização:**
  1. **Compreensão Teórica e Modelagem:** Discussão e mapeamento conceitual dos requisitos do enunciado com o capítulo de Programação Dinâmica e Maior Subsequência Comum (LCS) do livro *Introduction to Algorithms* (CLRS).
  2. **Implementação do Código:** Auxílio na escrita do código em Java com foco estrito em simplicidade acadêmica (uso de tipos primitivos, arrays, laços diretos e métodos claros, sem emprego de frameworks ou abstrações corporativas).
  3. **Tratamento de Robustez na Entrada:** Identificação e tratamento do caractere de BOM (*Byte Order Mark*) gerado em certos arquivos UTF-8 no Windows, garantindo que o programa leia arquivos em qualquer sistema operacional sem exceções.
  4. **Elaboração e Automação de Testes:** Formulação de casos de teste de borda (listas vazias em comum, ordens invertidas e teste de carga no limite superior de $1000 \times 1000$).
  5. **Estruturação da Documentação:** Apoio na organização formal das seções do relatório e na formatação para geração do documento PDF final.