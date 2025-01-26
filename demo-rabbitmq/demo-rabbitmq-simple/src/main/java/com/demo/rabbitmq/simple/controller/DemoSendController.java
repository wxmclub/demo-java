package com.demo.rabbitmq.simple.controller;

import com.demo.rabbitmq.simple.model.Student;
import com.demo.rabbitmq.simple.service.DemoSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2020-09-26
 */
@RestController
@RequestMapping("/demo/send")
public class DemoSendController {

    @Autowired
    private DemoSendService demoService;

    @PostMapping("/batch/message1")
    public Student batchSendMessage1(@RequestParam(value = "parallel", required = false, defaultValue = "1") int parallel,
                                     @RequestParam(value = "count", required = false, defaultValue = "10") int count,
                                     @RequestBody Student student) {
        demoService.batchSendMessage1(parallel, count, student);
        return student;
    }

    @PostMapping("/message1")
    public Student sendMessage1(@RequestBody Student student) {
        demoService.sendMessage1(student);
        return student;
    }

    @PostMapping("/message2")
    public Student sendMessage2(@RequestBody Student student) {
        demoService.sendMessage2(student);
        return student;
    }

    @PostMapping("/message3")
    public Student sendMessage3(@RequestBody Student student) {
        demoService.sendMessage3(student);
        return student;
    }

    @PostMapping("/message4")
    public Student sendMessage4(@RequestBody Student student) {
        demoService.sendMessage4(student);
        return student;
    }

}
