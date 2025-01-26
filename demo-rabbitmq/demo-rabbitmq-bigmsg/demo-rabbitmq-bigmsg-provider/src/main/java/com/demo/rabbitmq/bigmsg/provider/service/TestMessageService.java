package com.demo.rabbitmq.bigmsg.provider.service;

import com.demo.rabbitmq.bigmsg.provider.consts.MyConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2022-12-22
 */
@Slf4j
@Service
public class TestMessageService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String MSG_BASE = "1234567890";

    private static final String MSG_100;
    private static final String MSG_500;
    private static final String MSG_1KB;
    private static final String MSG_5KB;
    private static final String MSG_256KB;
    private static final String MSG_512KB;

    static {
        MSG_100 = createMsg(100);
        MSG_500 = createMsg(500);
        MSG_1KB = createMsg(1024);
        MSG_5KB = createMsg(5 * 1024);
        MSG_256KB = createMsg(256 * 1024);
        MSG_512KB = createMsg(512 * 1024);
    }

    static String createMsg(int size) {
        StringBuilder msg = new StringBuilder(size + 10);
        do {
            msg.append(MSG_BASE);
        } while (msg.length() < size);
        return msg.substring(0, size);
    }

    @Scheduled(cron = "0/1 * * * * *")
    public void sendMessages() {
        //log.info("sendMessages: 100: 600");
        //this.doSendMessages(600, MSG_100);

        //log.info("sendMessages: 500: 1000");
        //this.doSendMessages(1000, MSG_500);

        log.info("sendMessages: 1KB: 200");
        this.doSendMessages(200, MSG_1KB);

        //log.info("sendMessages: 5KB: 1000");
        //this.doSendMessages(1000, MSG_5KB);

        //log.info("sendMessages: 256KB: 100");
        //this.doSendMessages(100, MSG_256KB);

        //log.info("sendMessages: 512KB: 5");
        //this.doSendMessages(5, MSG_512KB);
    }

    void doSendMessages(int count,
                        String message) {
        for (int i = 0; i < count; i++) {
            this.sendMessage(message);
        }
    }

    void sendMessage(String message) {
        rabbitTemplate.convertAndSend(MyConsts.EXCHANGE_TEST, MyConsts.ROUTING_TEST, message);
    }

}
