package com.wms.service;

import com.wms.pojo.ParamKey;
import com.wms.pojo.Permissions;
import com.wms.service.base.BaseService;
import com.wms.vo.ParamKeyVo;

public interface ParamKeyService extends BaseService<ParamKey, ParamKeyVo> {

    ParamKey insertOrUpdate(ParamKey member);
}
