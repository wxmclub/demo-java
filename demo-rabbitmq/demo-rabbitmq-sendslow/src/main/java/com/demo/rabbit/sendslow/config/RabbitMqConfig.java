package com.demo.rabbit.sendslow.config;

import javax.annotation.PostConstruct;

import com.demo.rabbit.sendslow.consts.MyConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2024-09-05
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    @Autowired(required = false)
    private AmqpAdmin amqpAdmin;

    @PostConstruct
    public void init() {
        if (amqpAdmin == null) {
            log.debug("ignore declareQueue.");
            return;
        }

        log.info("declareQueue: {}", MyConst.QUEUE_ORDER);
        amqpAdmin.declareQueue(new Queue(MyConst.QUEUE_ORDER));
        log.info("declareQueue: {}", MyConst.QUEUE_PAIED);
        amqpAdmin.declareQueue(new Queue(MyConst.QUEUE_PAIED));
    }

}
