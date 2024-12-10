package com.wms.filter.login;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@TableName(value = "member")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO,value = "id")
    private Long id;

    @TableField(value = "username")
    private String username;
    @TableField(value = "password")
    private String password;
    @TableField(value = "sex")
    private int sex;
    @TableField(value = "age")
    private int age;
    @TableField(value = "email")
    private String email;
    @TableField(value = "phone")
    private String phone;
    @TableField(value = "address")
    private String address;
    @TableField(value = "birthday")
    private Date birthday;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "update_time")
    private Date updateTime;
    @TableField(value = "expiration_time")
    private Date expirationTime;
    @TableField(value = "create_member")
    private Long createMember;
    @TableField(value = "update_member")
    private Long updateMember;
    @TableField(value = "status")
    private int status;
    @TableField(value = "salt")
    private String salt; // 随机盐

}
