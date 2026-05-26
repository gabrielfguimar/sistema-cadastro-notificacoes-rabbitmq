package com.gabriel.msnotificador.consumer;

import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoConsumer {

    // 1. Listener da Fila Principal com amarração à Dead Letter Exchange
    @RabbitListener(queuesToDeclare = @Queue(
        name = "usuarios.v1.cadastro-criado", 
        durable = "true",
        arguments = {
            @Argument(name = "x-dead-letter-exchange", value = "usuarios.v1.cadastro-exchange.dlq"),
            @Argument(name = "x-dead-letter-routing-key", value = "notificacao-faiou")
        }
    ))
    public void receberNotificacao(String mensagem) {
        System.out.println("\n-------------------------------------------");
        System.out.println(">> [Fila Principal] Nova mensagem recebida!");
        
        String[] dados = mensagem.split(";");
        String nome = dados[0];
        String email = dados[1];

        System.out.println("ℹ️ Processando dados do usuário: " + nome + " (" + email + ")");
        
        // Simula a falha temporária para forçar o circuito da DLQ
        throw new RuntimeException("Falha de conexão temporária com a rede.");
    }

    // 2. Listener da Fila de Dead Letter (DLQ)
    @RabbitListener(queuesToDeclare = @Queue(name = "usuarios.v1.cadastro-criado.dlq", durable = "true"))
    public void receberMensagensComFalha(String mensagem) {
        System.out.println("\n🚨 [DLQ - ALERTA CRÍTICO] Mensagem encaminhada para a Dead Letter Queue!");
        System.out.println("📦 Payload com erro: " + mensagem);
        System.out.println("⚙️ Ação necessária: Salvando no banco de auditoria para análise técnica...");
        System.out.println("-------------------------------------------");
    }
}