package com.demo.rabbitmq.simple.service;

import java.util.concurrent.ExecutorService;

import com.demo.rabbitmq.simple.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2020-09-26
 */
@Slf4j
@Service
public class DemoSendService {

    private static final String EXCHANGE_SIMPLE = "demo.simple";

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ExecutorService executorService;

    public void batchSendMessage1(int parallel,
                                  int count,
                                  Student student) {
        this.batchExecutor(parallel, count, () -> rabbitTemplate.convertAndSend(EXCHANGE_SIMPLE, "test1", student));
    }

    public void sendMessage1(Student student) {
        rabbitTemplate.convertAndSend(EXCHANGE_SIMPLE, "test1", student);
    }

    public void sendMessage2(Student student) {
        //  单独设置消息属性
        rabbitTemplate.convertAndSend(EXCHANGE_SIMPLE, "test2", student, message -> {
            // 扩展消息属性，设置单独消息过期时间
            message.getMessageProperties().setExpiration("10000");
            return message;
        });
    }

    public void sendMessage3(Student student) {
        rabbitTemplate.convertAndSend(EXCHANGE_SIMPLE, "test3", student);
    }

    public void sendMessage4(Student student) {
        rabbitTemplate.convertAndSend(EXCHANGE_SIMPLE, "test4", student);
    }

    void batchExecutor(int parallel,
                       int count,
                       Runnable runnable) {
        for (int i = 0; i < parallel; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < count; j++) {
                    runnable.run();
                }
            });
        }
    }

}
