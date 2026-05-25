package com.gabriel.apicadastro.service;

import com.gabriel.apicadastro.config.RabbitMQConfig;
import com.gabriel.apicadastro.model.Usuario;
import com.gabriel.apicadastro.repository.UsuarioRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Usuario cadastrar(Usuario usuario) {
        // 1. Salva o registro no PostgreSQL
        Usuario usuarioSalvo = repository.save(usuario);

        // 2. Prepara a mensagem assíncrona (Nome;Email)
        String mensagem = usuarioSalvo.getNome() + ";" + usuarioSalvo.getEmail();

        // Agora enviamos para a EXCHANGE passando a ROUTING KEY, e não direto para o nome da fila
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_PRINCIPAL, "notificacao-sucesso", mensagem);
        System.out.println(">> Mensagem postada com sucesso: " + mensagem);

        return usuarioSalvo;
    }
}