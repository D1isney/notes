CREATE TABLE `wms-server`.`role_permissions`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint NULL COMMENT '角色',
  `permissions_id` bigint NULL COMMENT '权限',
  `create_time` datetime NULL COMMENT '创建时间',
  `update_time` datetime NULL COMMENT '更新时间',
  `create_member` bigint NULL COMMENT '创建人',
  `update_member` bigint NULL COMMENT '更新人',
  `remark` varchar(255) NULL COMMENT '描述',
  PRIMARY KEY (`id`)
);