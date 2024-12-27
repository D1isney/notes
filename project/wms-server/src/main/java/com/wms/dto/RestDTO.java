package com.wms.dto;

import com.wms.filter.login.Member;
import com.wms.pojo.Permissions;
import com.wms.pojo.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestDTO {
    private Member member;
    private Role role;
    private List<Permissions> permissions;
}
