package com.wms.dao;

import com.wms.pojo.Permissions;
import com.wms.service.base.IBaseMapper;
import com.wms.vo.PermissionsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface PermissionsDao extends IBaseMapper<Permissions, PermissionsVo> {
    List<String> getRemark();

    String lastCode();


    List<Permissions> queryByIds(@Param("ids") Long[] ids);
}
