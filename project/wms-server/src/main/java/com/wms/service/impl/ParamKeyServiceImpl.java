package com.wms.service.impl;

import com.wms.dao.ParamKeyDao;
import com.wms.exception.EException;
import com.wms.pojo.GoodsParam;
import com.wms.pojo.ParamKey;
import com.wms.service.GoodsParamService;
import com.wms.service.ParamKeyService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.thread.MemberThreadLocal;
import com.wms.utils.R;
import com.wms.utils.StringUtil;
import com.wms.vo.ParamKeyVo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ParamKeyServiceImpl extends IBaseServiceImpl<ParamKeyDao, ParamKey, ParamKeyVo> implements ParamKeyService {

    @Resource
    private ParamKeyDao paramKeyDao;

    @Lazy
    @Resource
    private GoodsParamService goodsParamService;
    ;

    @Override
    public R<?> insertOrUpdate(ParamKey paramKey) {
        if (paramKey.getId() == null) {
            // 新增
            add(paramKey);
            return R.ok("新增成功！");
        } else {
            //  修改
            modify(paramKey);
            return R.ok("修改成功！");
        }
    }

    public Long currentMember() {
        return MemberThreadLocal.get().getMember().getId();
    }

    public void modify(ParamKey paramKey) {
        ParamKey old = queryById(paramKey.getId());
        //  检查一下是不是修改了类型，修改了类型就需要先把原本的参数去掉
        if (!old.getType().equals(paramKey.getType())) {
            List<GoodsParam> goodsParamByParamKeyId = getGoodsParamByParamKeyId(old.getId());
            if (!StringUtil.isEmpty(goodsParamByParamKeyId)) {
                throw new EException("还有物料正在使用该参数，请先修改物料参数再进行删除！");
            }
        }

        paramKey.setUpdateTime(new Date());
        paramKey.setUpdateMember(currentMember());
        //  可以修改
        saveOrModify(paramKey);
    }

    public void add(ParamKey paramKey) {
        check(paramKey);
        Map<String, Object> map = new HashMap<>();
        map.put("key", paramKey.getKey());
        map.put("type", paramKey.getType());
        List<ParamKey> paramKeys = queryList(map);
        if (!StringUtil.isEmpty(paramKeys)) {
            throw new EException("该类型的key值已存在,请勿重复添加！");
        }
        paramKey.setCreateTime(new Date());
        paramKey.setCreateMember(currentMember());
        paramKey.setUpdateTime(new Date());
        paramKey.setUpdateMember(currentMember());
        saveOrModify(paramKey);
    }

    public void check(ParamKey paramKey) {
        if (Objects.isNull(paramKey)) {
            throw new EException("无效参数！");
        }
        if (Objects.isNull(paramKey.getType())) {
            throw new EException("无效参数，参数类型必须填写！");
        }
        if (Objects.isNull(paramKey.getKey())) {
            throw new EException("无效参数，参数Key必须填写！");
        }
        if (Objects.isNull(paramKey.getName())) {
            throw new EException("无效参数，参数名必须填写！");
        }
    }


    @Override
    public List<ParamKey> queryByIds(Long[] ids) {
        return paramKeyDao.queryByIds(ids);
    }

    /**
     * 删除参数列表，先检查有没有物料在使用这个参数
     *
     * @param ids 参数表id
     */
    @Override
    public void deleteParamKeyByIds(Long[] ids) {
        Arrays.stream(ids).forEach(paramKeyId -> {
            List<GoodsParam> goodsParamByParamKeyId = getGoodsParamByParamKeyId(paramKeyId);
            //  不等于空，直接报错
            if (!StringUtil.isEmpty(goodsParamByParamKeyId)) {
                throw new EException("还有物料正在使用该参数，请先修改物料参数再进行删除！");
            }
        });
        deleteByIds(ids);
    }

    /**
     * 根据物料类型来查询所有的参数
     *
     * @param type 物料类型
     * @param goodId 物料id
     * @return R
     */
    @Override
    public R<?> getParamKeyListByType(Integer type, Long goodId) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        //  关于这个类型的所有的参数
        List<ParamKey> paramKeys = queryList(map);
        //  通过goodId 找到所有该物料原本的参数
        map = new HashMap<>();
        map.put("goodsId", goodId);
        List<GoodsParam> goodsParams = goodsParamService.queryList(map);
        Stream<ParamKey> paramKeyStream = paramKeys.stream().filter(paramKey -> {
            List<GoodsParam> list = new ArrayList<>();
            goodsParams.forEach(param -> {
                if (paramKey.getId().equals(param.getParamId())) {
                    list.add(param);
                }
            });
            return list.isEmpty();
        });
        return R.ok(paramKeyStream);
    }

    @Override
    public R<?> getParamKeyListByType(Integer type) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        //  关于这个类型的所有的参数
        List<ParamKey> paramKeys = queryList(map);
        return R.ok(paramKeys);
    }

    public List<GoodsParam> getGoodsParamByParamKeyId(Long paramKeyId) {
        Map<String, Object> map = new HashMap<>();
        map.put("paramId", paramKeyId);
        return goodsParamService.queryList(map);
    }
}
