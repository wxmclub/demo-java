
CREATE DATABASE `demo_mybatis`;

USE `demo_mybatis`;

CREATE TABLE `t_account` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `mobile` bigint NOT NULL COMMENT '手机号',
  `email` varchar(63) NOT NULL COMMENT '邮箱',
  `nick_name` varchar(127) NOT NULL DEFAULT '' COMMENT '昵称',
  `sex` tinyint NOT NULL DEFAULT '0' COMMENT '0-未知,1-男,2-女',
  `score` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '积分',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间 ',
  `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_mobile` (`mobile`),
  UNIQUE KEY `uniq_email` (`email`),
  KEY `idx_modify_time` (`modify_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账号';