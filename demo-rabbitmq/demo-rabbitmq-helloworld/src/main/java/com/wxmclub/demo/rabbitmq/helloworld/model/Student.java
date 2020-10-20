package com.wxmclub.demo.rabbitmq.helloworld.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Student {

    private Integer id;

    private String name;

    private BigDecimal score;

}
