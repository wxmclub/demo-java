package com.demo.regex.breaker;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2025-01-21
 */
public class SystemClock {

    /**
     * 更新周期
     */
    private final long period;
    /**
     * 用来作为时间戳存储的容器
     */
    private final AtomicLong now;

    private SystemClock(long period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    private static SystemClock instance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * 获取毫秒时间戳 替换System.currentTimeMillis()
     */
    public static long currentTimeMillis() {
        return instance().now();
    }

    private long now() {
        return now.get();
    }

    /**
     * 初始化定时器
     */
    private void scheduleClockUpdating() {
        ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1, r -> {
            Thread thread = new Thread(r, "System Clock");
            // 设置为守护线程
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), period, period, TimeUnit.MILLISECONDS);
    }

    private static class InstanceHolder {
        //设置1ms更新一次时间
        static final SystemClock INSTANCE = new SystemClock(1L);
    }

}
