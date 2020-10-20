package com.wxmclub.demo.rabbitmq.helloworld.controller;

import javax.annotation.Resource;

import com.wxmclub.demo.rabbitmq.helloworld.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/send")
    public String sendMessage(@RequestBody Student student) {
        log.info("sendMessage: {}", student);
        rabbitTemplate.convertAndSend("queue.student", student);
        return "success";
    }

}
