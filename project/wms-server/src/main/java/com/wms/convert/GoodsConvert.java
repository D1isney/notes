package com.wms.convert;

import com.wms.dto.TypeAndValue;
import com.wms.exception.EException;
import com.wms.pojo.GoodsParam;
import com.wms.utils.StringUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class GoodsConvert {

    public List<GoodsParam> convertToGoodsParam(List<TypeAndValue> typeAndValues, Long currentMember){
        List<GoodsParam> goodsParams = new ArrayList<>();
        typeAndValues.forEach(typeAndValue -> {
            if (StringUtil.isEmpty(typeAndValue.getValue())) {
                throw new EException(typeAndValue.getText() + "参数必须有值！");
            }
            GoodsParam goodsParam = new GoodsParam();
            goodsParam.setParamId(typeAndValue.getParamId());
            goodsParam.setGoodsId(typeAndValue.getGoodId());
            goodsParam.setValue(typeAndValue.getValue());
            goodsParam.setCreateMember(currentMember);
            goodsParam.setUpdateMember(currentMember);
            goodsParam.setCreateTime(new Date());
            goodsParam.setUpdateTime(new Date());
            goodsParams.add(goodsParam);
        });
        return goodsParams;
    }
}
