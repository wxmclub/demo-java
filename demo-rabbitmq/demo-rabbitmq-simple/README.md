# demo-rabbitmq-simple 说明

## 1. 初始化说明

* Exchange

name        | type   | 备注
------------|--------|-----
demo.simple | direct |

* Queue

name               | 备注
-------------------|-----
demo.simple.test1  | 
demo.simple.test2  | {x-dead-letter-exchange:demo.simple, x-dead-letter-routing-key:test1}
demo.simple.test3  |
demo.simple.test4  |

* Binding

Exchange    | RoutingKey | Queue
------------|------------|------------------
demo.simple | test1      | demo.simple.test1
demo.simple | test2      | demo.simple.test2
demo.simple | test3      | demo.simple.test3
demo.simple | test4      | demo.simple.test4

## 2. 生产端


## 3. 消费端

