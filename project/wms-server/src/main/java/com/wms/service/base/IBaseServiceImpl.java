package com.wms.service.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.exception.EException;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class IBaseServiceImpl<M extends IBaseMapper<T,V>, T,V> extends ServiceImpl<M,T> implements BaseService<T,V>{


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(T t) {
        return getBaseMapper().insert(t);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBatch(Collection<T> collection) {
        return getBaseMapper().saveBatch(collection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(T t) {
        return getBaseMapper().update(t);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBatch(Collection<T> collection) {
        return getBaseMapper().updateBatch(collection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Serializable id) {
        return getBaseMapper().deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Serializable[] ids) {
        return getBaseMapper().deleteByIds(ids);
    }

    @Override
    public T queryById(Serializable id) {
        return getBaseMapper().queryById(id);
    }

    @Override
    public List<T> queryList(Map<String, Object> map) {
        return getBaseMapper().queryList(map);
    }

    @Override
    public long queryTotal(Map<String, Object> map) {
        return getBaseMapper().queryTotal(map);
    }

    @Override
    public long queryTotal() {
        return getBaseMapper().queryTotal();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T saveOrModify(T t) {
        if(!saveOrUpdate(t)){
            throw new EException("save or update fail!");
        }
        return t;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T persist(T t) {
        int save = getBaseMapper().save(t);
        return t;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateNotEmpty(T t) {
        return getBaseMapper().updateNotEmpty(t);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBatchNotEmpty(Collection<T> collection) {
        return getBaseMapper().updateBatchNotEmpty(collection);
    }

    @Override
    public List<V> list(Map<String, Object> params) {
        return getBaseMapper().list(params);
    }

    @Override
    public IPage<V> pageList(IPage<V> page, Map<String, Object> params) {
        return getBaseMapper().pageList(page,params);
    }

    @Override
    public List<T> queryByIds(Serializable[] ids) {
        return getBaseMapper().queryByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBatchById(T entity, Serializable[] ids) {
        return getBaseMapper().updateBatchById(entity,ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBatchByIdNotEmpty(T entity, Serializable[] ids) {
        return getBaseMapper().updateBatchByIdNotEmpty(entity,ids);
    }


}
