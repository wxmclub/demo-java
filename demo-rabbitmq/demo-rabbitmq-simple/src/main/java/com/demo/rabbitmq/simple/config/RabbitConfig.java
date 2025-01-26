package com.demo.rabbitmq.simple.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2022-01-17
 */
@Configuration
public class RabbitConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
