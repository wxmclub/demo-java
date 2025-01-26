package com.demo.rabbitmq.simple.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2023-03-28
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ExecutorService executorService() {
        // 当达到最大数时，使用主线程运行
        return new ThreadPoolExecutor(20, 20, 10, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
