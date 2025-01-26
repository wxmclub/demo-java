package com.demo.rabbit.sendslow.listener;

import com.demo.rabbit.sendslow.consts.MyConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2024-09-05
 */
@Slf4j
@ConditionalOnProperty(prefix = "project.listener", name = "enabled")
@Service
public class MyMessageListener {

    @RabbitListener(queues = MyConst.QUEUE_ORDER)
    public void receiveOrder(String message) {
        log.debug("receive message order: {}", message);
    }

    @RabbitListener(queues = MyConst.QUEUE_PAIED)
    public void receivePaied(String message) {
        log.debug("receive message paied: {}", message);
    }

}
