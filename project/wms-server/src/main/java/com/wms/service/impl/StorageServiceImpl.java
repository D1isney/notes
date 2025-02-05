package com.wms.service.impl;

import com.wms.dao.StorageDao;
import com.wms.pojo.Storage;
import com.wms.service.StorageService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.vo.StorageVo;
import org.springframework.stereotype.Service;

@Service
public class StorageServiceImpl extends IBaseServiceImpl<StorageDao, Storage, StorageVo> implements StorageService {
}
