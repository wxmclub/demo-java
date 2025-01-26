package com.demo.rabbitmq.jmh.service;

import java.util.concurrent.TimeUnit;

import com.demo.rabbitmq.jmh.DemoRabbitmqJmhApplication;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2022-02-25
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(1)
@State(value = Scope.Benchmark)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 5)
public class MqServiceJmhTest {

    private ConfigurableApplicationContext context;

    private MqService service;

    /**
     * setup初始化容器的时候只执行一次
     */
    @Setup(Level.Trial)
    public void init() {
        context = SpringApplication.run(DemoRabbitmqJmhApplication.class);

        service = context.getBean(MqService.class);
    }

    @TearDown(Level.Trial)
    public void end() {
        context.close();
    }

    @Benchmark
    public void testCreatMessage() {
        String name = "unit.jmh.test1";
        service.createMessage(name);
    }

    @Benchmark
    public void testCreatAndSendMessage() {
        String name = "unit.jmh.test1";
        String message = "{\"returncode\":0,\"message\":\"success\"}";
        service.createMessage(name);
        service.sendMessage(name, message);
    }

    @Benchmark
    public void testSendMessage() {
        String name = "unit.jmh.test1";
        String message = "{\"returncode\":0,\"message\":\"success\"}";
        service.sendMessage(name, message);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(MqService.class.getName() + ".*")
                .build();
        new Runner(options).run();
    }

}
