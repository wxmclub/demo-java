package com.demo.rabbitmq.bigmsg.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2022-12-22
 */
@EnableScheduling
@SpringBootApplication
public class DemoRabbitmqBigmsgConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoRabbitmqBigmsgConsumerApplication.class, args);
    }

}
