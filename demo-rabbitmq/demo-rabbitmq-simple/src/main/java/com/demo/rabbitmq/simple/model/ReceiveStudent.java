package com.demo.rabbitmq.simple.model;

import java.util.Date;

import lombok.Data;

/**
 * @author wxmclub
 * @version 1.0
 * @date 2023-06-07
 */
@Data
public class ReceiveStudent {

    private Integer id;

    private String name;

    private Date createTime;

}
