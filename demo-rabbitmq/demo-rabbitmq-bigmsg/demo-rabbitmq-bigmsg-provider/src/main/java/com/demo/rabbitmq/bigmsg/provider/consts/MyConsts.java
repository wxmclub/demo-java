package com.demo.rabbitmq.bigmsg.provider.consts;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2022-12-22
 */
public interface MyConsts {

    String EXCHANGE_TEST = "exchange.demo.rabbitmq";

    String ROUTING_TEST = "test.msg";

    String QUEUE_TEST = "demo.rabbitmq.bigmsg";

    String ROUTING_TEST_DEAD = "test.msg.dead";
    String QUEUE_TEST_DEAD = "demo.rabbitmq.bigmsg.dead";

}
