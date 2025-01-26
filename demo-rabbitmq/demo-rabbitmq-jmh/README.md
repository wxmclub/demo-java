# RabbitMQ 操作基准测试

## 1. 发送消息基准测试

* 测试方法说明
    * testCreatMessage: 创建Exchange, Queue, Binding, name=queueName
    * testSendMessage: 发送MQ消息
    * testCreatAndSendMessage: 包含testCreatMessage, testSendMessage

* 基准测试数据

```text
Benchmark                                 Mode  Cnt      Score      Error  Units
MqServiceJmhTest.testCreatAndSendMessage  avgt    5  11382.054 ± 3050.310  us/op
MqServiceJmhTest.testCreatMessage         avgt    5  10287.179 ± 1531.096  us/op
MqServiceJmhTest.testSendMessage          avgt    5    103.422 ±  116.332  us/op
```

* 结果说明
    * 创建Exchange、Queue、Binding相当耗时，比单纯发消息耗时增加100倍左右，不适合放在每次发送消息前调用
    * 创建功能还是适合放在程序初始化启动的时候调用
