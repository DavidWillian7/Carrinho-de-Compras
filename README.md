# Carrinho de Compras - Projeto de Teste de Software

Este projeto é uma API REST desenvolvida em Java + Spring Boot para simular o módulo de Carrinho de Compras do sistema Atlantic. O objetivo é realizar o checkout, registrando o usuário, os itens desejados e calculando o valor total a ser pago, incluindo o frete.

## Configuração do Projeto

### Requisitos
- Java JDK 8 ou superior
- Maven
- Git
- JUnit
- Mockito
- Spring Boot Test

### Executar o Projeto
mvn test CarrinhoServiceTest

### Executar Testes
mvn test

### Cobertura dos Testes
A cobertura dos testes pode ser verificada usando um relatório de cobertura. Execute o seguinte comando:

mvn clean verify

### Estrutura do Projeto
src/main/java/com/seunome/sobrenome/vendas: Contém as classes principais do projeto.
Controller: Lida com as requisições HTTP.
Service: Implementa a lógica de negócio.
Repository: Simula o acesso a dados (pode ser mockado para testes).
src/test/java/com/seunome/sobrenome/vendas: Contém classes de teste.
ServiceTest: Testes para a camada de serviço.

### Casos de Teste
Os casos de teste estão documentados em um arquivo separado no formato de planilha e podem ser encontrados em: 

### Componentes do Grupo
DAVID WILLIAN PEREIRA JATOBÁ
MARIANA LIMA SILVA
ALLAN PABLO LOPES DE ARAUJO
HALINE DIAS SANTOS
