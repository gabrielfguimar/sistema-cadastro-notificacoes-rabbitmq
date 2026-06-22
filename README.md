[![Voltar ao Perfil](https://img.shields.io/badge/-🔙_Ver_Perfil_Principal-blue?style=for-the-badge)](https://github.com/gabrielfguimar)

# Sistema de Cadastro e Notificações (Arquitetura Orientada a Eventos com RabbitMQ)

Este repositório apresenta um ecossistema de microsserviços modular, resiliente e totalmente conteinerizado, focado no fluxo de cadastro de usuários e disparos de notificações assíncronas. O projeto aplica padrões arquiteturais de alta disponibilidade e tolerância a falhas, simulando cenários reais de engenharia de software de nível sênior.

## 🏗️ Arquitetura do Sistema

O ecossistema é composto por dois microsserviços principais que se comunicam de forma assíncrona através do broker de mensageria **RabbitMQ**, utilizando o banco de dados **PostgreSQL** para persistência e **Docker** para a orquestração da infraestrutura.

```mermaid
graph LR
    Client[Cliente / HTTP POST] --> API[api-cadastro]
    API -->|Persiste Usuário| DB[(PostgreSQL)]
    API -->|Publica Evento| Exchange[usuarios.v1.cadastro-exchange]
    Exchange --> Queue[usuarios.v1.cadastro-criado]
    Queue --> MS[ms-notificador]
    
    subgraph "Circuito de Resiliencia DLQ"
        MS -->|Falha Critica / 4 Retries| DLX[usuarios.v1.cadastro-exchange.dlq]
        DLX --> DLQ[usuarios.v1.cadastro-criado.dlq]
        DLQ --> ListenerDLQ[Listener de Contingencia]
    end
```

---
### 🔙 Voltar ao meu perfil principal
Clique [aqui](https://github.com/gabrielfguimar) para ver meu portfólio completo, projetos e jornada técnica.
