package com.wms.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@TableName("permissions")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Permissions implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO,value = "id")
    private Long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "code")
    private String code;

    @TableField(value = "path")
    private String path;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "create_member")
    private Long createMember;
    @TableField(value = "update_member")
    private Long updateMember;

    @TableField(value = "remark")
    private String remark;
}
