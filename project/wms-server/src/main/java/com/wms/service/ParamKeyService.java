package com.wms.service;

import com.wms.pojo.ParamKey;
import com.wms.service.base.BaseService;
import com.wms.utils.R;
import com.wms.vo.ParamKeyVo;

import java.util.List;

public interface ParamKeyService extends BaseService<ParamKey, ParamKeyVo> {

    R<?> insertOrUpdate(ParamKey paramKey);

    List<ParamKey> queryByIds(Long[] ids);

    void deleteParamKeyByIds(Long[] ids);
}
