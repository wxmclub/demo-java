package com.demo.agent.interceptor;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2025-01-24
 */
public class AttachMain {

    public static void main(String[] args) throws Exception {
        // agentmain()方法所在jar包(绝对路径)
        String jar = "/[父绝对路径]/demo-agent/agent-interceptor/target/agent-interceptor-1.0-SNAPSHOT-jar-with-dependencies.jar";

        for (VirtualMachineDescriptor virtualMachineDescriptor : VirtualMachine.list()) {
            // 针对指定名称的JVM实例
            if ("com.demo.agent.test.AgentTest".equals(virtualMachineDescriptor.displayName())) {
                System.out.println("将对该进程的vm进行增强：com.demo.agent.test.AgentTest的vm进程, pid=" + virtualMachineDescriptor.id());
                // attach到新JVM
                VirtualMachine vm = VirtualMachine.attach(virtualMachineDescriptor);
                // 加载agentmain所在的jar包
                vm.loadAgent(jar, "Agent");
                // detach
                vm.detach();
            }
        }
    }

}
