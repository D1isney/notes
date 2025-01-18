package com.wms.service.impl;

import com.wms.dao.GoodsParamDao;
import com.wms.pojo.GoodsParam;
import com.wms.service.GoodsParamService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.vo.GoodsParamVo;
import org.springframework.stereotype.Service;

@Service
public class GoodsParamServiceImpl extends IBaseServiceImpl<GoodsParamDao, GoodsParam, GoodsParamVo> implements GoodsParamService {
}
