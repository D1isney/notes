CREATE TABLE `wms-server`.`member`  (
  `id` bigint NOT NULL COMMENT '主键',
  `username` varchar(50) NULL COMMENT '账号',
  `password` varchar(255) NULL COMMENT '密码',
  `name` varchar(50) NULL COMMENT '姓名',
  `sex` tinyint(1) NULL COMMENT '性别',
	`age` int NULL COMMENT '年龄',
	`email` varchar(100) NULL COMMENT '邮箱',
	`phone` varchar(100) NULL COMMENT '电话',
	`address` TEXT NULL COMMENT '地址',
  `birthday` datetime NULL COMMENT '出生日期',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `expiration_time` datetime NULL COMMENT '过期时间',
  `create_member` bigint NULL COMMENT '创建人',
  `update_member` bigint NULL COMMENT '更新人',
  `status` int NULL COMMENT '激活状态（0：封禁，1：可用，2：已过期）',
	`salt` VARCHAR(20) NULL COMMENT '随机盐',
  PRIMARY KEY (`id`)
);

ALTER TABLE `wms-server`.`member` 
ADD UNIQUE INDEX `member.username`(`username`) COMMENT '账号索引',
ADD UNIQUE INDEX `member.id`(`id`);