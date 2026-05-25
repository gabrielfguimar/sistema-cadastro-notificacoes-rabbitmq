# Sistema de Cadastro de Usuários Distribuído com Spring Boot & RabbitMQ

Este repositório apresenta uma arquitetura de microsserviços orientada a eventos (EDA) desenvolvida para demonstrar padrões avançados de resiliência, tolerância a falhas e mensageria assíncrona no ecossistema Java/Spring.

## 🏗️ Arquitetura do Sistema

O projeto é dividido em dois microsserviços principais que se comunicam de forma assíncrona através do broker de mensageria **RabbitMQ**:

1. **api-cadastro**: API REST responsável por receber as requisições de novos usuários, persistir os dados no banco de dados e publicar o evento `cadastro-criado` na exchange principal.
2. **ms-notificador**: Consumidor assíncrono que escuta a fila de eventos e realiza o processamento pesado de notificações (como disparos de e-mails de boas-vindas).

---

## 🛡️ Padrões de Resiliência Implementados

Para garantir que nenhuma mensagem ou dado de cadastro seja perdido em caso de instabilidades na rede ou queda de serviços externos, a arquitetura conta com uma esteira de tratamento de erros em camadas:

### 1. Retry Pattern (Tentativas Automáticas)
Configurado diretamente no `ms-notificador` via Spring AMQP. Caso ocorra uma falha transitória (como um timeout ou oscilação na API de e-mails), o sistema intercepta o erro e realiza até **3 tentativas automáticas** de processamento com intervalos programados de 1 segundo antes de invalidar a operação.

### 2. Dead Letter Queue (DLQ)
Caso o estoque de tentativas automáticas seja completamente esgotado e o erro persista, o RabbitMQ utiliza o conceito de **Dead Letter Exchange (DLX)** para desviar de forma limpa a mensagem defeituosa para uma fila de isolamento dedicada (`usuarios.v1.cadastro-criado.dlq`). Isso evita o descarte de dados e elimina o risco de *poison pills* travarem a fila principal em loops infinitos de reprocessamento.

---

## 🛠️ Tecnologias Utilizadas

* **Java 17 / Spring Boot 3**
* **Spring AMQP (RabbitMQ Integration)**
* **Docker / Docker Compose** (Orquestração do Broker)
* **Maven Wrapper**
