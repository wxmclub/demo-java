# RabbitMQ 发送耗时慢问题排查

## 1. 问题描述

## 2. 基础环境

### 2.1 MQ环境安装

```bash
cd docker
#
docker run -d --hostname my-rabbit --name my-rabbit -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=123 rabbitmq:3.7.24-management-alpine
docker run -d --hostname my-rabbit --name my-rabbit -v ./config:/var/lib/rabbitmq/config -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=123 rabbitmq:3.7.24-management-alpine
#
docker exec -it my-rabbit bash

```

* rabbitmq.config

```properties
# 允许客户端通信过程中的Channel最大数，不包含为0的特殊Channel。设置为0表示不限制，但这是一个危险值（应用程序会有Channel漏洞），使用更多Channel将增加消息代理的内存占用。默认值：channel_max = 2047
channel_max=100
# Channel通信的超时时间，单位毫秒。用于内部通信，由于消息传递协议的差异和限制，没有直接向客户端公开。默认值：channel_operation_timeout = 15000
# channel_operation_timeout=1500
```
