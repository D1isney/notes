package com.wms.service.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BaseService <T,V> extends IService<T> {
    /**
     * 保存
     * @param t
     */
    int insert(T t);

    /**
     * 批量保存
     * @param collection
     */
    int insertBatch(Collection<T> collection);

    /**
     * 保存
     * @param t
     * @return
     */
    int update(T t);

    /**
     * 批量保存
     * @param collection
     * @return
     */
    int updateBatch(Collection<T> collection);

    /**
     * 删除
     * @param id
     * @return
     */
    int delete(Serializable id);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(Serializable[] ids);

    /**
     * 通过ID查询
     * @param id
     * @return
     */
    T queryById(Serializable id);

    /**
     * 通过Map条件查询
     * @param map
     * @return
     */
    List<T> queryList(Map<String, Object> map);

    /**
     * 通过条件查询数量
     * @param map
     * @return
     */
    long queryTotal(Map<String, Object> map);

    /**
     * 查询单表总数
     * @return
     */
    long queryTotal();


    /**
     * 新增或修改
     * @return
     */
    T saveOrModify(T t);

    T persist(T t);

    int updateNotEmpty(T t);

    int updateBatchNotEmpty(Collection<T> collection);

    List<V> list(Map<String,Object> params);

    IPage<V> pageList(IPage<V> page,Map<String,Object> params);

    List<T> queryByIds(Serializable[] ids);

    int updateBatchById(T entity,Serializable[] ids);

    int updateBatchByIdNotEmpty(T entity,Serializable[] ids);
}
