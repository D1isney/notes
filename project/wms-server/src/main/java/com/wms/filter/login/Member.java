package com.wms.filter.login;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@TableName(value = "member")
@ApiModel(value="Member", description="")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "账号")
    @TableField("`username`")
    private String username;

    @ApiModelProperty(value = "密码")
    @TableField("`password`")
    private String password;

    @ApiModelProperty(value = "姓名")
    @TableField("`name`")
    private String name;

    @ApiModelProperty(value = "性别")
    @TableField("`sex`")
    private Boolean sex;

    @ApiModelProperty(value = "年龄")
    @TableField("`age`")
    private Integer age;

    @ApiModelProperty(value = "邮箱")
    @TableField("`email`")
    private String email;

    @ApiModelProperty(value = "电话")
    @TableField("`phone`")
    private String phone;

    @ApiModelProperty(value = "地址")
    @TableField("`address`")
    private String address;

    @ApiModelProperty(value = "出生日期")
    @TableField("`birthday`")
    private Date birthday;

    @ApiModelProperty(value = "创建时间")
    @TableField("`create_time`")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("`update_time`")
    private Date updateTime;

    @ApiModelProperty(value = "过期时间")
    @TableField("`expiration_time`")
    private Date expirationTime;

    @ApiModelProperty(value = "创建人")
    @TableField("`create_member`")
    private Long createMember;

    @ApiModelProperty(value = "更新人")
    @TableField("`update_member`")
    private Long updateMember;

    @ApiModelProperty(value = "激活状态（0：封禁，1：可用，2：已过期）")
    @TableField("`status`")
    private Integer status;

    @ApiModelProperty(value = "随机盐")
    @TableField("`salt`")
    private String salt;

}
