package com.demo.rabbitmq.bigmsg.provider.config;

import javax.annotation.PostConstruct;

import com.demo.rabbitmq.bigmsg.provider.consts.MyConsts;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2022-12-22
 */
@Configuration
public class RabbitMqConfig {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @PostConstruct
    public void init() {
        DirectExchange exchange = ExchangeBuilder.directExchange(MyConsts.EXCHANGE_TEST)
                .build();
        amqpAdmin.declareExchange(exchange);

        Queue queueBigmsgDead = QueueBuilder.durable(MyConsts.QUEUE_TEST_DEAD)
                .build();
        amqpAdmin.declareQueue(queueBigmsgDead);

        Binding bindingBigmsgDead = BindingBuilder.bind(queueBigmsgDead)
                .to(exchange)
                .with(MyConsts.ROUTING_TEST_DEAD);
        amqpAdmin.declareBinding(bindingBigmsgDead);


        Queue queueBigmsg = QueueBuilder.durable(MyConsts.QUEUE_TEST)
                .deadLetterExchange(MyConsts.EXCHANGE_TEST)
                .deadLetterRoutingKey(MyConsts.ROUTING_TEST_DEAD)
                .build();
        amqpAdmin.declareQueue(queueBigmsg);

        Binding bindingBigmsg = BindingBuilder.bind(queueBigmsg)
                .to(exchange)
                .with(MyConsts.ROUTING_TEST);
        amqpAdmin.declareBinding(bindingBigmsg);

    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
