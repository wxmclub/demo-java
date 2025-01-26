package com.demo.rabbitmq.simple.model;

import java.util.Date;

import lombok.Data;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2020-09-26
 */
@Data
public class Student {

    private Integer id;

    private String name;

    private Date createTime;

}
