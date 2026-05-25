package com.gabriel.apicadastro.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    // Nomes das Filas e Exchanges
    public static final String FILA_PRINCIPAL = "usuarios.v1.cadastro-criado";
    public static final String FILA_DLQ = "usuarios.v1.cadastro-criado.dlq";
    
    public static final String EXCHANGE_PRINCIPAL = "usuarios.v1.cadastro-exchange";
    public static final String EXCHANGE_DLQ = "usuarios.v1.cadastro-exchange.dlq";

    // 1. Criamos a Exchange Principal
    @Bean
    public DirectExchange exchangePrincipal() {
        return new DirectExchange(EXCHANGE_PRINCIPAL);
    }

    // 2. Criamos a Exchange da DLQ (Para onde as mensagens mortas vão)
    @Bean
    public DirectExchange exchangeDLQ() {
        return new DirectExchange(EXCHANGE_DLQ);
    }

    // 3. Criamos a Fila Principal CONFIGURADA com ponte para a DLQ
    @Bean
    public Queue filaPrincipal() {
        Map<String, Object> argumentos = new HashMap<>();
        // Diz para a fila principal qual exchange ela deve acionar se a mensagem "morrer"
        argumentos.put("x-dead-letter-exchange", EXCHANGE_DLQ);
        // Define a rota exata que a mensagem morta vai usar ao ir para a DLQ
        argumentos.put("x-dead-letter-routing-key", "notificacao-faiou");
        
        return new Queue(FILA_PRINCIPAL, true, false, false, argumentos);
    }

    // 4. Criamos a Fila de DLQ (Apenas uma fila comum que vai guardar os erros)
    @Bean
    public Queue filaDLQ() {
        return new Queue(FILA_DLQ, true);
    }

    // 5. Vinculamos (Binding) a Fila Principal com a Exchange Principal
    @Bean
    public Binding bindingPrincipal() {
        return BindingBuilder.bind(filaPrincipal()).to(exchangePrincipal()).with("notificacao-sucesso");
    }

    // 6. Vinculamos a Fila DLQ com a Exchange DLQ
    @Bean
    public Binding bindingDLQ() {
        return BindingBuilder.bind(filaDLQ()).to(exchangeDLQ()).with("notificacao-faiou");
    }
}