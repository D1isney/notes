package com.wms.dao;

import com.wms.pojo.ParamKey;
import com.wms.service.base.IBaseMapper;
import com.wms.vo.ParamKeyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ParamKeyDao extends IBaseMapper<ParamKey, ParamKeyVo> {
    List<ParamKey> queryByIds(@Param("ids") Long[] ids);
}
