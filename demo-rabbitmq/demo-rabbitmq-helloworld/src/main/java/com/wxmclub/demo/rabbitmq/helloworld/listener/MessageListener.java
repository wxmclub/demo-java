package com.wxmclub.demo.rabbitmq.helloworld.listener;

import com.wxmclub.demo.rabbitmq.helloworld.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageListener {

    @RabbitListener(queues = {"queue.student"})
    public void receiveMessage(Student student) {
        log.info("receiveMessage: {}", student);
    }

}
