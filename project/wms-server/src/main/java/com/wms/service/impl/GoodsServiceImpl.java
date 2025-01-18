package com.wms.service.impl;

import com.wms.dao.GoodsDao;
import com.wms.dto.TypeAndValue;
import com.wms.pojo.Goods;
import com.wms.pojo.GoodsParam;
import com.wms.pojo.ParamKey;
import com.wms.service.GoodsParamService;
import com.wms.service.GoodsService;
import com.wms.service.ParamKeyService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.utils.CodeUtils;
import com.wms.vo.GoodsVo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl extends IBaseServiceImpl<GoodsDao, Goods, GoodsVo> implements GoodsService {

    @Resource
    private GoodsDao goodsDao;

    @Lazy
    @Resource
    private GoodsParamService goodsParamService;

    @Lazy
    @Resource
    private ParamKeyService paramKeyService;

    /**
     * 最后的Code
     *
     * @return code
     */
    @Override
    public String lastCode() {
        return CodeUtils.getString(goodsDao.lastCode());
    }


    /**
     * 通过物料ID找到对应的参数
     *
     * @param goodsId 物料id
     * @return 物料参数信息
     */
    @Override
    public List<TypeAndValue> getTypeAndValue(Long goodsId) {
        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", goodsId);
        //  所有的参数关系 以及 value
        List<GoodsParam> goodsParams = goodsParamService.queryList(map);
        if (Objects.isNull(goodsParams)) {
            return null;
        } else {
            return createTypeAndValue(goodsId, goodsParams);
        }
    }

    /**
     * 创建所有的关系
     *
     * @param goodsId     物料id
     * @param goodsParams 关系
     * @return list
     */
    public List<TypeAndValue> createTypeAndValue(Long goodsId, List<GoodsParam> goodsParams) {
        //  拿到所有物料关系表的参数id
        Long[] ids = goodsParams.stream()
                .map(GoodsParam::getParamId)
                .toArray(Long[]::new);
        //  找出来
        List<ParamKey> paramKeys = paramKeyService.queryByIds(ids);
        //  参数表id
        Map<Long, ParamKey> paramKeyMap = paramKeys.stream()
                .collect(Collectors.toMap(ParamKey::getId, key -> key));
        //  比较取出所有关于这个关系的所有
        return goodsParams.stream()
                .filter(param -> paramKeyMap.containsKey(param.getParamId()))
                .map(param -> {
                    TypeAndValue typeAndValue = new TypeAndValue();
                    ParamKey paramKey = paramKeyMap.get(param.getParamId());
                    typeAndValue.setParamId(param.getParamId());
                    typeAndValue.setGoodId(goodsId);
                    typeAndValue.setName(paramKey.getName());
                    typeAndValue.setText(paramKey.getKey());
                    typeAndValue.setValue(param.getValue());
                    return typeAndValue;
                })
                .collect(Collectors.toList());
    }
}
