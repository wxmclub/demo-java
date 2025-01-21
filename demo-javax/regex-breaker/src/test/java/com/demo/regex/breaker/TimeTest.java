package com.demo.regex.breaker;

import org.apache.commons.lang3.time.StopWatch;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2025-01-21
 */
public class TimeTest {

    public static void main(String[] args) {
        // 执行一百次循环
        for (int t = 0; t < 100; t++) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            // 获取一千万次时间
            for (int i = 0; i < 10000000; i++) {
                System.currentTimeMillis();
                //SystemClock.currentTimeMillis();
            }
            stopWatch.stop();
            long totalTimeMillis = stopWatch.getTime();
            System.out.println(totalTimeMillis);
        }
    }

}
