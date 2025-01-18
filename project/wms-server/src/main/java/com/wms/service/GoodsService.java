package com.wms.service;

import com.wms.dto.TypeAndValue;
import com.wms.pojo.Goods;
import com.wms.service.base.BaseService;
import com.wms.vo.GoodsVo;

import java.util.List;

public interface GoodsService extends BaseService<Goods, GoodsVo> {

    String lastCode();

    List<TypeAndValue> getTypeAndValue(Long goodsId);
}
