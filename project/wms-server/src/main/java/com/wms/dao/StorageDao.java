package com.wms.dao;

import com.wms.pojo.Storage;
import com.wms.service.base.IBaseMapper;
import com.wms.vo.StorageVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StorageDao extends IBaseMapper<Storage, StorageVo> {
    String lastCode();
}
