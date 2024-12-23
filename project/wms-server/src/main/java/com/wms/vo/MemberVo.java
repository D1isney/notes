package com.wms.vo;


import com.wms.filter.login.Member;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;


@ApiModel(value = "MemberVo", description = "包装类")
@Getter
@Setter
public class MemberVo extends Member {
    private String createUsername;
    private String updateUsername;
}
