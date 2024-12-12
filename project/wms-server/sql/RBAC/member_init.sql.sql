CREATE TABLE `wms-server`.`member`
(
    `id`              BIGINT NOT NULL COMMENT '主键',
    `username`        VARCHAR(50) NULL COMMENT '账号',
    `password`        VARCHAR(255) NULL COMMENT '密码',
    `name`            VARCHAR(50) NULL COMMENT '姓名',
    `sex`             TINYINT ( 1 ) NULL COMMENT '性别',
    `age`             INT NULL COMMENT '年龄',
    `email`           VARCHAR(100) NULL COMMENT '邮箱',
    `phone`           VARCHAR(100) NULL COMMENT '电话',
    `address`         TEXT NULL COMMENT '地址',
    `birthday`        DATETIME NULL COMMENT '出生日期',
    `create_time`     DATETIME NULL COMMENT '创建时间',
    `update_time`     DATETIME NULL COMMENT '更新时间',
    `expiration_time` DATETIME NULL COMMENT '过期时间',
    `create_member`   BIGINT NULL COMMENT '创建人',
    `update_member`   BIGINT NULL COMMENT '更新人',
    `status`          INT NULL COMMENT '激活状态（0：封禁，1：可用，2：已过期）',
    `salt`            VARCHAR(20) NULL COMMENT '随机盐',
    `online`          TINYINT ( 1 ) NULL COMMENT '在线状态' PRIMARY KEY ( `id` )
);
ALTER TABLE `wms-server`.`member`
    ADD UNIQUE INDEX `member.username` ( `username` ) COMMENT '账号索引',
    ADD UNIQUE INDEX `member.id` ( `id` );