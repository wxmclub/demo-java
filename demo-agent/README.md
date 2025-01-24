# java-agent


## 运行

### 使用 java agent 静态加载方式实现

```bash
# 添加VM参数(使用相对路径可能会报错：Error opening zip file or JAR manifest missing)
-javaagent:/[父绝对路径]/agent-interceptor/target/agent-interceptor-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### 使用 java agent 动态加载方式实现

* 参考：`com.demo.agent.interceptor.AttachMain`方式，通过`VirtualMachine`动态加载

## 参考文档

* [【JVM】Java agent超详细知识梳理](https://juejin.cn/post/7157684112122183693)
* [Java Agent (JVM Instrumentation 机制) 极简教程](https://cloud.tencent.com/developer/article/1813421)
* [手把手教你实现一个Java Agent](https://mp.weixin.qq.com/s/-7SKqe8NEjBkAV2U6LzEPQ)
