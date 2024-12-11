package com.wms.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
@TableName(value = "log")
@ApiModel(value = "LogRecord")
public class LogRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增主键")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "内容")
    @TableField("`message`")
    private String message;

    @ApiModelProperty(value = "调用用户ID")
    @TableField("`member_id`")
    private Long memberId;

    @ApiModelProperty(value = "调用时间")
    @TableField("`create_time`")
    private Date createTime;

    @ApiModelProperty(value = "日志级别（0：普通日志，1：警告日志，2：危险日志，3：报警日志）")
    @TableField("`type`")
    private Integer type;

    @ApiModelProperty(value = "调用接口")
    @TableField("`path`")
    private String path;

    @ApiModelProperty(value = "接口参数")
    @TableField("`params`")
    private String params;

    @ApiModelProperty(value = "返回参数")
    @TableField("`result`")
    private String result;

    @ApiModelProperty(value = "执行时长")
    @TableField("`execute_time`")
    private Long executeTime;




}
