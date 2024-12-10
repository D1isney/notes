CREATE TABLE `wms-server`.`log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `message` varchar(255) NULL COMMENT '内容',
  `member_id` bigint NULL COMMENT '调用用户ID',
  `create_time` datetime NULL COMMENT '调用时间',
  `type` int NULL COMMENT '日志级别（0：普通日志，1：警告日志，2：危险日志，3：报警日志）',
  `path` varchar(255) NULL COMMENT '调用接口',
  `params` text NULL COMMENT '接口参数',
  `result` longtext NULL COMMENT '返回参数',
	`execute_time` BIGINT NULL COMMENT '执行时长',
  PRIMARY KEY (`id`)
);