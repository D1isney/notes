CREATE TABLE `wms-server`.`member_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `member_id` bigint NULL COMMENT '用户',
  `role_id` bigint NULL COMMENT '角色',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `create_member` datetime NULL COMMENT '创建人',
  `update_member` datetime NULL COMMENT '更新人',
  `remark` varchar(255) NULL COMMENT '描述',
  PRIMARY KEY (`id`)
);