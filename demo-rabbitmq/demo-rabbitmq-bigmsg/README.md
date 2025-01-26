# RabbitMq大消息消费异常问题

## 1. MQ配置说明

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        concurrency: 1     # 消费线程数
        max-concurrency: 2 # 最大的消费线程数
        prefetch: 50       # 每次预取消息条数
```

### 1.1 `prefetch`

* 表示单个消费者每次预取消息个数
* `prefetch`在1.x中默认值是`1`，这可能会导致高效使用者的利用率不足。从2.x开始默认值改为`250`，这将使消费者在大多数常见场景中保持忙碌，从而提高吞吐量。

> 每个customer会在MQ预取一些消息放入内存的LinkedBlockingQueue中，这个值越高，消息传递的越快，但非顺序处理消息的风险更高。如果ack模式为none，则忽略。如有必要，将增加此值以匹配txSize或messagePerAck。从2.0开始默认为250；设置为1将还原为以前的行为。


### 1.2 `concurrency`

* 表示每个Listener开启几个线程来消费
* 根据项目实例数、消息积压情况适当调整`max-concurrency`，不宜过大，因为现在整个经销商是公用1个MQ集群，整个集群有最大连接数包括channel数的限制，过大会影响整个经销商集群的稳定性
* 不能超过: `spring.rabbitmq.cache.channel.size`，默认为`CachingConnectionFactory.DEFAULT_CHANNEL_CACHE_SIZE=25`

## 2. 消费MQ消息时偶发乱码

### 2.1 问题描述

* 项目在消费MQ消息时偶发报错，消息中出现乱码，造成使用Jackson2JsonMessageConverter解析消息失败
* 消息长度文本最长的有3000+，再加上header，单条消息长度最长应该在4000+

* 错误日志

```text
2022-12-22 16:03:07.211  WARN 24372 --- [ntContainer#0-3] s.a.r.l.ConditionalRejectingErrorHandler : Execution of Rabbit message listener failed.
org.springframework.amqp.rabbit.support.ListenerExecutionFailedException: Failed to convert message
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.onMessage(MessagingMessageListenerAdapter.java:146) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.doInvokeListener(AbstractMessageListenerContainer.java:1630) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.actualInvokeListener(AbstractMessageListenerContainer.java:1549) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.invokeListener(AbstractMessageListenerContainer.java:1537) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.doExecuteListener(AbstractMessageListenerContainer.java:1528) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.executeListener(AbstractMessageListenerContainer.java:1472) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer.doReceiveAndExecute(SimpleMessageListenerContainer.java:977) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer.receiveAndExecute(SimpleMessageListenerContainer.java:923) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer.access$1600(SimpleMessageListenerContainer.java:83) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer$AsyncMessageProcessingConsumer.mainLoop(SimpleMessageListenerContainer.java:1298) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer$AsyncMessageProcessingConsumer.run(SimpleMessageListenerContainer.java:1204) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_202]
Caused by: org.springframework.amqp.support.converter.MessageConversionException: Failed to convert Message content
	at org.springframework.amqp.support.converter.AbstractJackson2MessageConverter.doFromMessage(AbstractJackson2MessageConverter.java:322) ~[spring-amqp-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.support.converter.AbstractJackson2MessageConverter.fromMessage(AbstractJackson2MessageConverter.java:273) ~[spring-amqp-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.support.converter.AbstractJackson2MessageConverter.fromMessage(AbstractJackson2MessageConverter.java:253) ~[spring-amqp-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.adapter.AbstractAdaptableMessageListener.extractMessage(AbstractAdaptableMessageListener.java:302) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter$MessagingMessageConverterAdapter.extractPayload(MessagingMessageListenerAdapter.java:342) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.support.converter.MessagingMessageConverter.fromMessage(MessagingMessageConverter.java:130) ~[spring-amqp-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.toMessagingMessage(MessagingMessageListenerAdapter.java:224) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.onMessage(MessagingMessageListenerAdapter.java:136) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	... 11 common frames omitted
Caused by: com.fasterxml.jackson.core.JsonParseException: Illegal unquoted character ((CTRL-CHAR, code 5)): has to be escaped using backslash to be included in string value
 at [Source: (String)""{\"name\":\"testLong\",\"value\":\"12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234"[truncated 2540 chars]; line: 1, column: 1226]
	at com.fasterxml.jackson.core.JsonParser._constructError(JsonParser.java:1851) ~[jackson-core-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.core.base.ParserMinimalBase._reportError(ParserMinimalBase.java:707) ~[jackson-core-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.core.base.ParserBase._throwUnquotedSpace(ParserBase.java:1046) ~[jackson-core-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.core.json.ReaderBasedJsonParser._finishString2(ReaderBasedJsonParser.java:2098) ~[jackson-core-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.core.json.ReaderBasedJsonParser._finishString(ReaderBasedJsonParser.java:2069) ~[jackson-core-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.core.json.ReaderBasedJsonParser.getText(ReaderBasedJsonParser.java:294) ~[jackson-core-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.databind.deser.std.StringDeserializer.deserialize(StringDeserializer.java:35) ~[jackson-databind-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.databind.deser.std.StringDeserializer.deserialize(StringDeserializer.java:10) ~[jackson-databind-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.databind.ObjectMapper._readMapAndClose(ObjectMapper.java:4526) ~[jackson-databind-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.databind.ObjectMapper.readValue(ObjectMapper.java:3468) ~[jackson-databind-2.11.4.jar:2.11.4]
	at org.springframework.amqp.support.converter.AbstractJackson2MessageConverter.convertBytesToObject(AbstractJackson2MessageConverter.java:345) ~[spring-amqp-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.support.converter.AbstractJackson2MessageConverter.doFromMessage(AbstractJackson2MessageConverter.java:310) ~[spring-amqp-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	... 18 common frames omitted

2022-12-22 16:03:07.212  WARN 24372 --- [ntContainer#0-3] ingErrorHandler$DefaultExceptionStrategy : Fatal message conversion error; message rejected; it will be dropped or routed to a dead letter exchange, if so configured: (Body:'"{\"name\":\"testLong\",\"value\":\"1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901����678901234567���234567890123���890123456789����456789012345���012345678901����67890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567���234567890123���890123456789����456789012345���012345678901����678901234567���234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\"}"' MessageProperties [headers={__TypeId__=java.lang.String}, contentType=application/json, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, redelivered=false, receivedExchange=exchange.demo.rabbitmq, receivedRoutingKey=test.msg, deliveryTag=1295, consumerTag=amq.ctag-c_EIepUnBPgXUlappWeAOA, consumerQueue=demo.rabbitmq.bigmsg])
2022-12-22 16:03:07.214 ERROR 24372 --- [ntContainer#0-3] o.s.a.r.l.SimpleMessageListenerContainer : Execution of Rabbit message listener failed, and the error handler threw an exception
org.springframework.amqp.AmqpRejectAndDontRequeueException: Error Handler converted exception to fatal
	at org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler.handleError(ConditionalRejectingErrorHandler.java:116) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.invokeErrorHandler(AbstractMessageListenerContainer.java:1434) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.handleListenerException(AbstractMessageListenerContainer.java:1718) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.executeListener(AbstractMessageListenerContainer.java:1493) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer.doReceiveAndExecute(SimpleMessageListenerContainer.java:977) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer.receiveAndExecute(SimpleMessageListenerContainer.java:923) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer.access$1600(SimpleMessageListenerContainer.java:83) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer$AsyncMessageProcessingConsumer.mainLoop(SimpleMessageListenerContainer.java:1298) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer$AsyncMessageProcessingConsumer.run(SimpleMessageListenerContainer.java:1204) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_202]
Caused by: org.springframework.amqp.rabbit.support.ListenerExecutionFailedException: Failed to convert message
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.onMessage(MessagingMessageListenerAdapter.java:146) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.doInvokeListener(AbstractMessageListenerContainer.java:1630) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.actualInvokeListener(AbstractMessageListenerContainer.java:1549) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.invokeListener(AbstractMessageListenerContainer.java:1537) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.doExecuteListener(AbstractMessageListenerContainer.java:1528) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer.executeListener(AbstractMessageListenerContainer.java:1472) [spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	... 6 common frames omitted
Caused by: org.springframework.amqp.support.converter.MessageConversionException: Failed to convert Message content
	at org.springframework.amqp.support.converter.AbstractJackson2MessageConverter.doFromMessage(AbstractJackson2MessageConverter.java:322) ~[spring-amqp-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.support.converter.AbstractJackson2MessageConverter.fromMessage(AbstractJackson2MessageConverter.java:273) ~[spring-amqp-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.support.converter.AbstractJackson2MessageConverter.fromMessage(AbstractJackson2MessageConverter.java:253) ~[spring-amqp-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.adapter.AbstractAdaptableMessageListener.extractMessage(AbstractAdaptableMessageListener.java:302) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter$MessagingMessageConverterAdapter.extractPayload(MessagingMessageListenerAdapter.java:342) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.support.converter.MessagingMessageConverter.fromMessage(MessagingMessageConverter.java:130) ~[spring-amqp-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.toMessagingMessage(MessagingMessageListenerAdapter.java:224) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter.onMessage(MessagingMessageListenerAdapter.java:136) ~[spring-rabbit-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	... 11 common frames omitted
Caused by: com.fasterxml.jackson.core.JsonParseException: Illegal unquoted character ((CTRL-CHAR, code 5)): has to be escaped using backslash to be included in string value
 at [Source: (String)""{\"name\":\"testLong\",\"value\":\"12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234"[truncated 2540 chars]; line: 1, column: 1226]
	at com.fasterxml.jackson.core.JsonParser._constructError(JsonParser.java:1851) ~[jackson-core-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.core.base.ParserMinimalBase._reportError(ParserMinimalBase.java:707) ~[jackson-core-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.core.base.ParserBase._throwUnquotedSpace(ParserBase.java:1046) ~[jackson-core-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.core.json.ReaderBasedJsonParser._finishString2(ReaderBasedJsonParser.java:2098) ~[jackson-core-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.core.json.ReaderBasedJsonParser._finishString(ReaderBasedJsonParser.java:2069) ~[jackson-core-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.core.json.ReaderBasedJsonParser.getText(ReaderBasedJsonParser.java:294) ~[jackson-core-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.databind.deser.std.StringDeserializer.deserialize(StringDeserializer.java:35) ~[jackson-databind-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.databind.deser.std.StringDeserializer.deserialize(StringDeserializer.java:10) ~[jackson-databind-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.databind.ObjectMapper._readMapAndClose(ObjectMapper.java:4526) ~[jackson-databind-2.11.4.jar:2.11.4]
	at com.fasterxml.jackson.databind.ObjectMapper.readValue(ObjectMapper.java:3468) ~[jackson-databind-2.11.4.jar:2.11.4]
	at org.springframework.amqp.support.converter.AbstractJackson2MessageConverter.convertBytesToObject(AbstractJackson2MessageConverter.java:345) ~[spring-amqp-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	at org.springframework.amqp.support.converter.AbstractJackson2MessageConverter.doFromMessage(AbstractJackson2MessageConverter.java:310) ~[spring-amqp-2.2.18.RELEASE.jar:2.2.18.RELEASE]
	... 18 common frames omitted
```

### 2.2 问题重现

#### 2.2.1 测试数据

* 测试时长: 10分钟
* 在消息不积压的情况下，设置最小的`max-concurrency`，由于测试的消费程序无任何逻辑，实际使用中还是要根据消费速度适当调整`prefetch`与`max-concurrency`

* 办公网下，连接测试环境rabbitMQ服务

消息长度 | 每秒发送条数 | 配置值 | 是否复现
---------|---------------|--------|------------------
100      | 600     | `prefetch=50`,`max-concurrency=10`  | 否
100      | 600     | `prefetch=100`,`max-concurrency=10` | 是(消费速度在1000/s左右时出现比较多)，消费端经常报连接错误：`Channel shutdown: connection error`
1KB      | 200     | `prefetch=1`,`max-concurrency=5`   | 否
1KB      | 200     | `prefetch=10`,`max-concurrency=1`  | 否，消费端偶尔报连接错误：`Channel shutdown: connection error`
1KB      | 200     | `prefetch=10`,`max-concurrency=5`  | 是，消费端偶尔报连接错误：`Channel shutdown: connection error`
5KB      | 50      | `prefetch=1`,`max-concurrency=2`   | 暂未复现，明天继续测试
5KB      | 50      | `prefetch=2`,`max-concurrency=2`   | 是
5KB      | 50      | `prefetch=1`,`max-concurrency=5`   | 是
5KB      | 200     | `prefetch=1`,`max-concurrency=10`  | 是
5KB      | 200     | `prefetch=30`,`max-concurrency=5`  | 是
256KB    | 2       | `prefetch=1`,`max-concurrency=1`   | 是
256KB    | 5       | `prefetch=1`,`max-concurrency=5`   | 是

* 使用本机docker部署的rabbitMQ服务

消息长度 | 每秒发送条数 | 配置值 | 是否复现
---------|---------------|--------|------------------
5KB      | 1000    | `prefetch=200`,`max-concurrency=5` | 否
256KB    | 100     | `prefetch=20`,`max-concurrency=5`  | 否，发送速度在60/s，效率明显降低

#### 2.2.2 总结

* RabbitMQ单条消息长度是没有限制的，但客户端与RabbitMQ服务端的最大帧是128K，超过底层会有拆包组包处理，会降低效率，网上推荐消息长度不超过4MB。
* 使用本机rabbitMQ服务测试时，没有出现过消息乱码问题；在办公网连接测试环境rabbitMQ服务，消息长度较大或接收消息数量较大时，都会偶发的出现消息乱码问题，而且这两个值越大，出现频率越多。所以可以确定是由于传输网络层的问题。

### 1.3 解决方案

* 发送端
    * 对消息进行拆分，比如向dataSave消息，每个类型单独一个队列，减少单个队列下的消息数量。
    * 降低消息长度，把原来的大消息，拆分成多条短消息，尽量减少产生大消息。
* 消费端
    * 原消费队列增加死信队列配置，解析失败的消息会丢弃或者放入死信队列，可以防止消息丢失。
    * 降低`prefetch`配置值，即每次预取消息个数，如: `prefetch=1`，但是这样会降低消费吞吐率，根据消息积压情况适当调整`max-concurrency`，但`concurrency`不宜设置过大
    * 也可以单独在监听方法上配置线程数`concurrency`，会覆盖全局配置，如：`@RabbitListener(concurrency = "5")`
    * 把发送和消费服务拆分，单独部署消费服务，单实例可以调低prefetch、concurrency，保证单实例消费消息速度不要过高，可以通过增加实例数提高消费速度。

## 参考文档

* (SpringBoot2.x下RabbitMQ的并发参数（concurrency和prefetch）)[https://cloud.tencent.com/developer/article/1956259]
* (RabbitMQ消息大小限制，队列长度限制)[https://blog.csdn.net/cuibin1991/article/details/107930479]