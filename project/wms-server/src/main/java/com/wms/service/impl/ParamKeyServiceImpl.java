package com.wms.service.impl;

import com.wms.dao.ParamKeyDao;
import com.wms.exception.EException;
import com.wms.pojo.GoodsParam;
import com.wms.pojo.ParamKey;
import com.wms.service.GoodsParamService;
import com.wms.service.ParamKeyService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.utils.StringUtil;
import com.wms.vo.ParamKeyVo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ParamKeyServiceImpl extends IBaseServiceImpl<ParamKeyDao, ParamKey, ParamKeyVo> implements ParamKeyService {

    @Resource
    private ParamKeyDao paramKeyDao;

    @Lazy
    @Resource
    private GoodsParamService goodsParamService;;

    @Override
    public ParamKey insertOrUpdate(ParamKey member) {
        return saveOrModify(member);
    }

    @Override
    public List<ParamKey> queryByIds(Long[] ids) {
        return paramKeyDao.queryByIds(ids);
    }

    /**
     * 删除参数列表，先检查有没有物料在使用这个参数
     * @param ids 参数表id
     */
    @Override
    public void deleteParamKeyByIds(Long[] ids) {
        Arrays.stream(ids).forEach(paramKeyId->{
            List<GoodsParam> goodsParamByParamKeyId = getGoodsParamByParamKeyId(paramKeyId);
            //  不等于空，直接报错
            if (!StringUtil.isEmpty(goodsParamByParamKeyId)){
                throw new EException("还有物料正在使用该参数，请先修改物料参数再进行删除！");
            }
        });
        deleteByIds(ids);
    }

    public List<GoodsParam> getGoodsParamByParamKeyId(Long paramKeyId) {
        Map<String,Object> map = new HashMap<>();
        map.put("paramId", paramKeyId);
        return goodsParamService.queryList(map);
    }
}
