package com.wms.dao;

import com.wms.pojo.ParamKey;
import com.wms.service.base.IBaseMapper;
import com.wms.vo.ParamKeyVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ParamKeyDao extends IBaseMapper<ParamKey, ParamKeyVo> {
}
