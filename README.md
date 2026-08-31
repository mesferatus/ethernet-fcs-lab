# Inspector de Quadros Ethernet

## Sobre o projeto

Este projeto demonstra, de forma visual, como o FCS ajuda a verificar se uma carga útil chegou inteira ao receptor.

O trabalho implementa o CRC-32 em Java e inclui uma pequena interface em JavaFX para representar o caminho do quadro: primeiro o transmissor calcula e anexa o FCS; depois o receptor calcula novamente o CRC e decide se aceita ou descarta os dados.

## Como testar

1. No transmissor, escreva uma mensagem e clique em **GERAR FCS**.
2. O programa mostra os bytes ASCII e o FCS anexado ao quadro.
3. No receptor, altere um caractere do payload, como `STATUS:OK` para `STATUS!OK`.
4. Clique em **VALIDAR QUADRO**.
5. O quadro original será aceito. O quadro alterado será descartado porque o CRC recalculado será diferente.

Também comparei o resultado da minha implementação com a classe `java.util.zip.CRC32`, como solicitado no enunciado.

Para o exemplo do exercício:

- `STATUS:OK` gera o FCS `9F81EB6E`;
- `STATUS!OK` gera o CRC `8FF2A7FF`.

## Arquivos

- `src/main/java/Crc32Ethernet.java`: algoritmo CRC-32 feito manualmente;
- `src/main/java/FcsApplication.java`: interface JavaFX e simulação do transmissor e receptor;
- `src/main/resources/style.css`: cores e aparência da aplicação;
- `pom.xml`: configuração usada pelo Maven para compilar e executar o JavaFX.

## Como executar

É necessário ter JDK 17 ou superior e Maven instalado:

```bash
mvn javafx:run
```
