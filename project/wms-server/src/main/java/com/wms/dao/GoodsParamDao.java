package com.wms.dao;

import com.wms.pojo.GoodsParam;
import com.wms.service.base.IBaseMapper;
import com.wms.vo.GoodsParamVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsParamDao extends IBaseMapper<GoodsParam, GoodsParamVo> {
}
