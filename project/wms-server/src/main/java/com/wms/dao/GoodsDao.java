package com.wms.dao;

import com.wms.pojo.Goods;
import com.wms.service.base.IBaseMapper;
import com.wms.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsDao extends IBaseMapper<Goods, GoodsVo> {
    String lastCode();

    List<Goods> queryGoodsByIds(@Param("ids") Long[] ids);
}
