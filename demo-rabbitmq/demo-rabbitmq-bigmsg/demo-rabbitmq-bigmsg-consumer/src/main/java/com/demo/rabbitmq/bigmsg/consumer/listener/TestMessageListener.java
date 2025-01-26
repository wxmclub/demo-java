package com.demo.rabbitmq.bigmsg.consumer.listener;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.demo.rabbitmq.bigmsg.consumer.consts.MyConsts;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2022-12-22
 */
@Slf4j
@Service
public class TestMessageListener {

    private Map<Long, String> errorMsg = new ConcurrentHashMap<>();

    @Autowired
    private MessageConverter messageConverter;

    @RabbitListener(queues = MyConsts.QUEUE_TEST, ackMode = "MANUAL")
    public void receiveLongMessage(Channel channel,
                                   @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                                   String message) throws IOException {
        try {
            log.debug("receive message: {}", message);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("receive message error: {}", e.getMessage(), e);
            channel.basicNack(deliveryTag, false, true);
        }
    }

    //@RabbitListener(queues = MyConsts.QUEUE_TEST, ackMode = "MANUAL")
    //public void receiveLongMessage(Channel channel,
    //                               Message message) throws IOException {
    //    try {
    //        if (errorMsg.containsKey(message.getMessageProperties().getDeliveryTag())) {
    //            log.info("重新接收乱码消息: deliveryTag={}, messageId={}",
    //                    message.getMessageProperties().getDeliveryTag(), message.getMessageProperties().getMessageId());
    //            errorMsg.remove(message.getMessageProperties().getDeliveryTag());
    //        }
    //        String msg = (String) messageConverter.fromMessage(message);
    //        log.debug("receive message: {}", msg);
    //        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    //    } catch (MessageConversionException e) {
    //        errorMsg.put(message.getMessageProperties().getDeliveryTag(), message.getMessageProperties().getMessageId());
    //        log.error("message conversion error: deliveryTag={}, messageId={}, error={}",
    //                message.getMessageProperties().getDeliveryTag(), message.getMessageProperties().getMessageId(), e.getMessage(), e);
    //        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
    //    } catch (Exception e) {
    //        log.error("receive message error: {}", e.getMessage(), e);
    //        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
    //    }
    //}

}
