CREATE TABLE `wms-server`.`role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(50) NULL COMMENT '角色名称',
	`code` varchar(50) NULL COMMENT '角色编码',
  `status` int NULL COMMENT '该角色状态（0：不可用角色，1：可用角色）',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `create_member` bigint NULL COMMENT '创建人',
  `update_member` bigint NULL COMMENT '更新人',
  `remark` varchar(255) NULL COMMENT '描述',
  PRIMARY KEY (`id`)
);