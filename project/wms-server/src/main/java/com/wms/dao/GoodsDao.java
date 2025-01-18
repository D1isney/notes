package com.wms.dao;

import com.wms.pojo.Goods;
import com.wms.service.base.IBaseMapper;
import com.wms.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsDao extends IBaseMapper<Goods, GoodsVo> {
    String lastCode();
}
