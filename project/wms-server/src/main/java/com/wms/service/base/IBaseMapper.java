package com.wms.service.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IBaseMapper<T,V> extends BaseMapper<T> {

    int save(@Param("entity") T t);

    /**
     * 批量保存
     * @param collection
     */
    int saveBatch(Collection<T> collection);

    /**
     * 保存
     * @param t
     * @return
     */
    int update(@Param("entity") T t);

    /**
     * 批量保存
     * @param collection
     * @return
     */
    int updateBatch(Collection<T> collection);



    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") Serializable[] ids);


    /**
     * 通过Map条件查询
     * @param params
     * @return
     */
    List<T> queryList(@Param("query") Map<String, Object> params);

    /**
     * 通过条件查询数量
     * @param params
     * @return
     */
    long queryTotal(@Param("query") Map<String, Object> params);

    /**
     * 查询单表总数
     * @return
     */
    long queryTotal();

    T queryById(Serializable id);

    int updateNotEmpty(@Param("entity")T t);

    int updateBatchNotEmpty(Collection<T> collection);

    List<T> queryByIds(Serializable[] ids);

    List<V> list(@Param("query") Map<String,Object> params);

    IPage<V> pageList(IPage<V> page,@Param("query") Map<String,Object> params);

    int updateBatchById(@Param("entity") T entity, Serializable[] ids);

    int updateBatchByIdNotEmpty(@Param("entity") T entity,Serializable[] ids);
}
