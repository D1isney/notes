package com.wms.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 *
 */
@Setter
@Getter
public class Query extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public static final String PAGE = "page";
    public static final String LIMIT = "limit";
    public static final String OFFSET = "offset";
    public static final String ORDER = "order";
    public static final String SIDX = "sidx";


    //当前页码
    private int page;
    //每页条数
    private int limit;

    //查询内容
    private String sSearch;

    private String sEcho;

    public Query(Map<String, Object> params){
        this();
        sp2Array(params);
        this.putAll(params);
        //分页参数
        initPage();
        sqlFilter();
        clearEmptyValue();
//        clearEmptyValue();
    }

    private void sp2Array(Map<String, Object> params){
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            if(stringObjectEntry.getValue() instanceof String && ((String)stringObjectEntry.getValue()).contains(",")){
                String[] split = ((String) stringObjectEntry.getValue()).split(",");
                params.put(stringObjectEntry.getKey(),split);
            }
        }
    }

    public Query() {
        super(10);
    }

    public Query timeStart(String...timeStart){
        if(timeStart == null) return this;
        for (String s : timeStart) {
            if(!StringUtil.isEmpty(get(s))) put(s,get(s) + DateConstant.START_TIME);
        }
        return this;
    }

    public Query timeEnd(String...timeEnd){
        if(timeEnd == null) return this;
        for (String s : timeEnd) {
            if(!StringUtil.isEmpty(get(s))) put(s,get(s) + DateConstant.LAST_TIME);
        }
        return this;
    }

    public static Query query(){
        return new Query(new HashMap<>());
    }


    public static Query empty(){
        return new Query();
    }

    public Query sqlFilter(){
        parseDataTableSortingParams(this);
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String sidx = get(SIDX)==null?"":get(SIDX).toString();
        String order = get(ORDER)==null?"":get(ORDER).toString();
        put(SIDX, SQLFilter.sqlInject(sidx));
        put(ORDER, SQLFilter.sqlInject(order));
        return this;
    }

    public Query initPage(){
        if(get(PAGE) != null){
            page = Integer.parseInt(get(PAGE).toString());
        }
        if(page < 1){
            page = 1;
        }
        if(get(LIMIT) != null){
            limit = Integer.parseInt(get(LIMIT).toString());
        }
        if(limit < 1){
            limit = 20;
        }
        put(OFFSET, (page - 1) * limit);
        put(PAGE, page);
        put(LIMIT, limit);
        return this;
    }

    public Query clearPage(){
        remove(OFFSET);
        remove(LIMIT);
        return this;
    }

    public Query and(String key,Object value){
        put(key,value);
        return this;
    }

    public Query clearEmptyValue(){
        CollectionUtil.mapEntry(this,entry -> {
            Map.Entry<String, Object> next = entry.next();
            if(StringUtil.isEmpty(next.getValue())) entry.remove();
            return false;
        });
        return this;
    }




    /**
     * 传入一个Class消除java语法检查的警告
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Page<T> getIPage(Class<T> clazz){
        clearPage();
        return new Page<>(page,limit);
    }

    /**datatable 排序参数改造
     * @Author huan
     * */
    private void parseDataTableSortingParams(Map<String, Object> params) {
        String sortCol=(String)params.get("iSortCol_0");
        if (StringUtils.isBlank(sortCol)||"0".equals(sortCol))
            return;
        String sidx=(String)params.get("mDataProp_"+sortCol);
        String order=(String)params.get("sSortDir_0");
        params.put(SIDX,sidx);
        params.put(ORDER,order);
    }


}
