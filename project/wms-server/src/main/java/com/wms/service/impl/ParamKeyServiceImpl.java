package com.wms.service.impl;

import com.wms.dao.ParamKeyDao;
import com.wms.pojo.ParamKey;
import com.wms.service.ParamKeyService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.vo.ParamKeyVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ParamKeyServiceImpl extends IBaseServiceImpl<ParamKeyDao, ParamKey, ParamKeyVo> implements ParamKeyService {

    @Resource
    private ParamKeyDao paramKeyDao;

    @Override
    public ParamKey insertOrUpdate(ParamKey member) {
        return saveOrModify(member);
    }
}
