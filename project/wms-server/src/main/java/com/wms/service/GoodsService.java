package com.wms.service;

import com.wms.dto.TypeAndValue;
import com.wms.pojo.Goods;
import com.wms.service.base.BaseService;
import com.wms.utils.R;
import com.wms.vo.GoodsVo;

import java.util.List;

public interface GoodsService extends BaseService<Goods, GoodsVo> {

    String lastCode();

    List<TypeAndValue> getTypeAndValue(Long goodsId);

    R<?> saveOrUpdateGoods(Goods goods);

    void deleteGoodsByIds(Long[] ids);

    Goods getGoodsByCode(String code);

    Goods getGoodsById(Long id);

    R<?> billOfMaterial();
}
