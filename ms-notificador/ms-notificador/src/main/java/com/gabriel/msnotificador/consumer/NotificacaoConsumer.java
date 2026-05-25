package com.gabriel.msnotificador.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoConsumer {

    @RabbitListener(queues = "usuarios.v1.cadastro-criado")
    public void consumirMensagem(String mensagem) {
        System.out.println("\n--------------------------------------------------");
        System.out.println(">> [Fila Principal] Nova mensagem recebida!");
        
        // Separando os dados que vieram no padrão "Nome;Email"
        String[] dados = mensagem.split(";");
        String nome = dados[0];
        String email = dados[1];
        
        System.out.println("ℹ️ Processando dados do usuário...");
        System.out.println("📧 Enviando e-mail de boas-vindas para: " + nome + " (" + email + ")");
        System.out.println("✅ Notificação realizada com absoluto sucesso!");
        System.out.println("--------------------------------------------------");
    }
}