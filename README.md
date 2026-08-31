<h1 align="center">
  # ☾ Ethernet FCS Lab ☽
  </h1>
<br> 

Aplicação JavaFX criada para demonstrar, de forma visual, como o FCS verifica a integridade de uma carga útil em um quadro Ethernet.

<br> 

## ✦ Sumário

- [Sobre o projeto](#sobre-o-projeto)
- [Objetivo da atividade](#objetivo-da-atividade)
- [Tecnologias](#tecnologias)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Pré-requisitos](#pré-requisitos)
- [Como executar](#como-executar)
- [Como testar](#como-testar)
- [Funcionamento do CRC-32](#funcionamento-do-crc-32)
- [Resultados do exemplo](#resultados-do-exemplo)
- [Decisões do projeto](#decisões-do-projeto)

## ✦ Sobre o projeto

O projeto simula o comportamento da subcamada MAC durante a validação de um quadro. O transmissor recebe um payload, calcula o CRC-32 e anexa o resultado como FCS. O receptor recebe o payload, calcula o CRC novamente e compara os valores.

Quando os valores coincidem, o quadro é aceito. Quando um caractere é alterado, o CRC muda e o quadro é descartado.

## ✦ Objetivo da atividade

O enunciado solicita uma implementação em Java do CRC-32 usando o polinômio Ethernet reverso `0xEDB88320`, além da comparação com `java.util.zip.CRC32`.

Além do requisito principal, foi criada uma interface JavaFX para tornar visíveis as etapas do cálculo e facilitar a simulação de uma corrupção de dados.

## ✦ Tecnologias

| Tecnologia | Uso no projeto |
| --- | --- |
| Java 17 | Linguagem principal |
| JavaFX 21 | Interface gráfica |
| Maven | Compilação e execução |
| CRC-32 | Verificação de integridade |

## ✦ Estrutura do projeto

```text
ethernet-fcs-lab/
├── src/
│   └── main/
│       ├── java/
│       │   ├── Crc32Ethernet.java
│       │   └── FcsApplication.java
│       └── resources/
│           └── style.css
├── .gitignore
├── pom.xml
└── README.md
```

## ✦ Pré-requisitos

- JDK 17 ou superior;
- Maven;
- sistema operacional com suporte à execução do JavaFX.

## ✦ Como executar

Na raiz do projeto, execute:

```bash
mvn javafx:run
```

A pasta `target` é criada automaticamente pelo Maven durante a compilação. Ela não faz parte da entrega e está ignorada pelo Git.

## ✦ Como testar

1. No painel **Transmissor**, informe uma mensagem.
2. Clique em **Gerar FCS**.
3. Confira os bytes ASCII e o FCS anexado.
4. No painel **Receptor**, altere um caractere do payload recebido.
5. Clique em **Validar quadro**.

O payload original deve ser aceito. O payload alterado deve ser descartado.

## ✦ Funcionamento do CRC-32

O algoritmo começa com o registrador em `0xFFFFFFFF`. Cada byte é aplicado usando XOR e seus oito bits são processados com deslocamento para a direita. Quando o bit menos significativo é `1`, é aplicado o polinômio reverso Ethernet `0xEDB88320`.

Ao final, o registrador passa por outro XOR com `0xFFFFFFFF` e o resultado é apresentado em hexadecimal com oito dígitos.

## ✦ Resultados do exemplo

Para o payload solicitado no enunciado:

| Payload | Bytes ASCII | FCS / CRC-32 |
| --- | --- | --- |
| `STATUS:OK` | `53 54 41 54 55 53 3A 4F 4B` | `9F81EB6E` |
| `STATUS!OK` | `53 54 41 54 55 53 21 4F 4B` | `8FF2A7FF` |

O resultado manual para `STATUS:OK` coincide com o resultado da classe `java.util.zip.CRC32`.

## ✦ Decisões do projeto

A implementação manual foi mantida separada da interface para deixar claro onde está a lógica exigida pela atividade. A classe padrão do Java é usada somente como referência de conferência.

A interface separa transmissor e receptor porque isso representa melhor o fluxo descrito no enunciado. O FCS gerado pelo transmissor permanece fixo enquanto o payload recebido pode ser editado, permitindo simular a corrupção de um único caractere.

O nome **Ethernet FCS Lab** foi escolhido porque o projeto funciona como um pequeno laboratório de testes, e não apenas como um programa que imprime um resultado no console.
