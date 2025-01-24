package com.deni.agent.test;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2025-01-22
 */
public class AgentTest {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            System.out.println("process result: " + process());
            Thread.sleep(5000);
        }
    }

    public static String process() {
        System.out.println("process!");
        return "success";
    }

}
