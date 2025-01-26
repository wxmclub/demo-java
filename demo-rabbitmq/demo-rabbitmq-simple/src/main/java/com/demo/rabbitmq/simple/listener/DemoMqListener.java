package com.demo.rabbitmq.simple.listener;

import java.io.IOException;
import java.util.Optional;

import com.demo.rabbitmq.simple.model.ReceiveStudent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2020-09-26
 */
@Slf4j
@Component
public class DemoMqListener {

    @Autowired
    private ObjectMapper objectMapper;

    // region 自动消费

    @RabbitListener(queues = {"demo.simple.test1"})
    public void receiveMessage1Auto(ReceiveStudent student) {
        log.info("receive message: test1: {}", this.serializeMessage(student));
    }

    // endregion

    // region 手动消费

    @RabbitListener(queues = {"demo.simple.test3"}, ackMode = "MANUAL")
    public void receiveMessage3Manual(Channel channel,
                                      @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                                      @Header("__TypeId__") String typeId,
                                      ReceiveStudent student) throws IOException {
        log.info("receive message: test3: deliveryTag={}, message={}, typeId={}, receiveClass={}"
                , tag, this.serializeMessage(student), typeId, student.getClass());
        try {
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("receive message: manual1: deliveryTag={}, error={}", tag, e.getMessage(), e);
            channel.basicNack(tag, false, false);
        }
    }

    @RabbitListener(queues = {"demo.simple.test4"}, ackMode = "MANUAL")
    public void receiveMessage4Manual(Channel channel,
                                      Message<ReceiveStudent> message) throws IOException {
        ReceiveStudent student = message.getPayload();
        long tag = Optional.ofNullable(message.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class)).orElse(0L);
        log.info("receive message: test4: deliveryTag={}, message={}", tag, this.serializeMessage(student));
        try {
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("receive message: manual2: deliveryTag={}, error={}", tag, e.getMessage(), e);
            channel.basicNack(tag, false, false);
        }
    }

    // endregion

    private String serializeMessage(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            log.error("serializeMessage error: {}", e.getMessage(), e);
            return null;
        }
    }

}
