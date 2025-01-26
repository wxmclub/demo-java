package com.demo.rabbitmq.jmh.service;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2022-02-24
 */
@Service
public class MqService {

    @Autowired
    private AmqpAdmin rabbitAdmin;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void createMessage(String name) {
        DirectExchange exchange = this.declareExchange(name);
        Queue queue = this.declareQueue(name);
        this.declareBinding(exchange, queue, name);
    }

    public DirectExchange declareExchange(String name) {
        DirectExchange exchange = ExchangeBuilder.directExchange(name).build();
        rabbitAdmin.declareExchange(exchange);
        return exchange;
    }

    public Queue declareQueue(String name) {
        Queue queue = QueueBuilder.durable(name).build();
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

    public void declareBinding(DirectExchange exchange,
                               Queue queue,
                               String routingKey) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);
    }

    public void sendMessage(String name,
                            String message) {
        rabbitTemplate.convertAndSend(name, name, message);
    }

}
