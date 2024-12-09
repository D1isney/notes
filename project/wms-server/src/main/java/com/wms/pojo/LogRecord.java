package com.wms.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
@TableName(value = "log")
public class LogRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO,value = "id")
    private Long id;

    @TableField(value = "message")
    private String message;

    @TableField(value = "member_id")
    private Long memberId;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "type")
    private Integer type;

    @TableField(value = "path")
    private String path;

    @TableField(value = "params")
    private String params;

    @TableField(value = "result")
    private String result;

    @TableField(value = "execute_time")
    private Long executeTime;




}
