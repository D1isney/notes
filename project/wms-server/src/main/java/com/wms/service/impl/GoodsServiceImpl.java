package com.wms.service.impl;

import com.wms.convert.GoodsConvert;
import com.wms.dao.GoodsDao;
import com.wms.dto.TypeAndValue;
import com.wms.exception.EException;
import com.wms.helper.CurrentHelper;
import com.wms.pojo.Goods;
import com.wms.pojo.GoodsParam;
import com.wms.pojo.ParamKey;
import com.wms.service.GoodsParamService;
import com.wms.service.GoodsService;
import com.wms.service.ParamKeyService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.utils.CodeUtils;
import com.wms.utils.R;
import com.wms.utils.StringUtil;
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

    @Resource
    private CurrentHelper currentHelper;

    @Resource
    private GoodsConvert goodsConvert;

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
        if (StringUtil.isEmpty(goodsParams)) {
            return null;
        } else {
            return createTypeAndValue(goodsId, goodsParams);
        }
    }

    @Override
    public R<?> saveOrUpdateGoods(Goods goods) {
        if (StringUtil.isEmpty(goods.getId())) {
            //  创建
            createGoods(goods);
            return R.ok("创建成功！");
        } else {
            //  更新
            updateGoods(goods);
            return R.ok("修改成功！");
        }
    }

    /**
     * 根据物料id删除物料以及物料参数关系
     *
     * @param ids 物料ids
     */
    @Override
    public void deleteGoodsByIds(Long[] ids) {
        Map<String, Object> map = new HashMap<>();
        Arrays.stream(ids).forEach(id -> {
            map.put("goodsId", id);
            List<GoodsParam> goodsParams = goodsParamService.queryList(map);
            Long[] listIds = goodsParams.stream().map(GoodsParam::getId).toArray(Long[]::new);
            if (listIds.length > 0) {
                goodsParamService.deleteByIds(listIds);
            }
        });
        //  删除物料
        deleteByIds(ids);
    }

    /**
     * 更新物料信息
     *
     * @param goods 物料
     */
    public void updateGoods(Goods goods) {
        Goods old = queryById(goods.getId());
        if (!old.getType().equals(goods.getType())) {
            if (!StringUtil.isEmpty(goods.getParams())) {
                throw new EException("请先删除所有参数再对物料类型进行修改！");
            }
        }
        //  更新物料
        saveOrModify(goods);
        //  更新这个物料的关系
        //  通过物料id拿到所有物料的关系
        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", goods.getId());
        List<GoodsParam> goodsParamsList = goodsParamService.queryList(map);
        //  前端传进来的所有的关系
        List<TypeAndValue> params = goods.getParams();
        //  删除旧的关系 以及添加新的关系
        deleteOldParamAndAddNewParam(params, goodsParamsList);

    }


    /**
     * 删除旧的关系，添加新的关系以及更新值
     *
     * @param params          前端参数
     * @param goodsParamsList 旧参数
     */
    public void deleteOldParamAndAddNewParam(List<TypeAndValue> params, List<GoodsParam> goodsParamsList) {
        if (StringUtil.isEmpty(params) && !StringUtil.isEmpty(goodsParamsList)) {
            Long[] collect = goodsParamsList.stream().map(GoodsParam::getId).toArray(Long[]::new);
            goodsParamService.deleteByIds(collect);
        } else if (StringUtil.isEmpty(params) && StringUtil.isEmpty(goodsParamsList)) {
            return;
        } else {
            Set<Long> paramsParamIds = params.stream().map(TypeAndValue::getParamId).collect(Collectors.toSet());
            Set<Long> goodsParamsParamIds = goodsParamsList.stream().map(GoodsParam::getParamId).collect(Collectors.toSet());
            List<TypeAndValue> newParamsToAdd = params.stream()
                    .filter(param -> !goodsParamsParamIds.contains(param.getParamId()))
                    .collect(Collectors.toList());
            List<GoodsParam> oldParamsToDelete = goodsParamsList.stream()
                    .filter(goodsParam -> !paramsParamIds.contains(goodsParam.getParamId()))
                    .collect(Collectors.toList());
            List<GoodsParam> paramsToUpdate = goodsParamsList.stream()
                    .filter(goodsParam -> paramsParamIds.contains(goodsParam.getParamId()))
                    .collect(Collectors.toList());
            for (GoodsParam goodsParam : paramsToUpdate) {
                TypeAndValue matchingParam = params.stream()
                        .filter(param -> param.getParamId().equals(goodsParam.getParamId()))
                        .findFirst()
                        .orElse(null);
                if (matchingParam != null) {
                    if (StringUtil.isEmpty(matchingParam.getValue())) {
                        throw new EException(matchingParam.getText() + "参数必须有值！");
                    }
                    // 更新 value
                    goodsParam.setValue(matchingParam.getValue());
                    goodsParam.setUpdateTime(new Date());
                    goodsParam.setUpdateMember(currentHelper.getCurrentMemberId());
                }
            }
            //  旧参数需要删除的
            if (!oldParamsToDelete.isEmpty()) {
                Long[] idsToDelete = oldParamsToDelete.stream().map(GoodsParam::getId).toArray(Long[]::new);
                goodsParamService.deleteByIds(idsToDelete);
            }
            //  新参数需要添加的
            if (!newParamsToAdd.isEmpty()) {
                List<GoodsParam> goodsParams = goodsConvert.convertToGoodsParam(newParamsToAdd, currentHelper.getCurrentMemberId());
                goodsParamService.saveOrUpdateBatch(goodsParams);
            }
            if (!paramsToUpdate.isEmpty()) {
                goodsParamService.saveOrUpdateBatch(paramsToUpdate);
            }

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


    /**
     * 创建物料
     *
     * @param goods 物料
     */
    public void createGoods(Goods goods) {
        //  检查物料有没有参数
        if (StringUtil.isEmpty(goods.getParams())) {
            create(goods);
        } else {
            create(goods);
            //  保存参数关系
            List<GoodsParam> goodsParams = goodsConvert.convertToGoodsParam(goods.getParams(), currentHelper.getCurrentMemberId());
            goodsParamService.saveOrUpdateBatch(goodsParams);
        }
    }

    public Goods create(Goods goods) {
        //  创建物料
        goods.setCode(lastCode());
        goods.setCreateMember(currentHelper.getCurrentMemberId());
        goods.setUpdateMember(currentHelper.getCurrentMemberId());
        goods.setCreateTime(new Date());
        goods.setUpdateTime(new Date());
        return saveOrModify(goods);
    }
}
