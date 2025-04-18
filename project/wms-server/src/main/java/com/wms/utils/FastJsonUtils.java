package com.wms.utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
* fastjson工具类
* @version:1.0.0
*/
public class FastJsonUtils {

	public static final SerializeConfig config;
 
    static {
        config = new SerializeConfig();
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
        config.put(Timestamp.class, new ObjectSerializer() {
			
			@Override
			public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
					throws IOException {
				if (object == null) {
		    		//serializer.out.writeNull();
		    		return;
		    	}
		    	
				Timestamp date = (Timestamp) object;
		        

		        serializer.write(DateUtil.format(date, DateConstant.DATE_TIME_PATTERN));
			}
		});
        config.put(Object.class, new ObjectSerializer() {

            @Override
            public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
                    throws IOException {
                if (object == null) {
                    return;
                }
            }
        });
    }
 
    public static final SerializerFeature[] features = {
    		SerializerFeature.DisableCircularReferenceDetect, //防止循环引用
    		//SerializerFeature.WriteMapNullValue, // 输出空置字段
           // SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
           // SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
     //       SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
          //  SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
    };
    
    public static String convertObjectToJSON(Object object) {
        return JSON.toJSONString(object, config, features);
    }
    
    public static String toJSONNoFeatures(Object object) {
        return JSON.toJSONString(object, config);
    }
    
 
 
    public static Object toBean(String text) {
        return JSON.parse(text);
    }
 
    public static <T> T toBean(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }
 
    /**
     *  转换为数组
     * @param text
     * @return
     */
    public static <T> Object[] toArray(String text) {
        return toArray(text, null);
    }
 
    /**
     *  转换为数组
     * @param text
     * @param clazz
     * @return
     */
    public static <T> Object[] toArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz).toArray();
    }
 
    /**
     * 转换为List
     * @param text
     * @param clazz
     * @return
     */
    public static <T> List<T> toList(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }
 
    /**
     * 将string转化为序列化的json字符串
     * @param text
     * @return
     */
    public static Object textToJson(String text) {
        Object objectJson  = JSON.parse(text);
        return objectJson;
    }
    
    /**
     * json字符串转化为map
     * @param s
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <K, V> Map<K, V>  stringToCollect(String s) {
        Map<K, V> m = (Map<K, V>) JSONObject.parseObject(s);
        return m;
    }
    
    /**
     * 转换JSON字符串为对象
     * @param jsonData
     * @param clazz
     * @return
     */
    public static Object convertJsonToObject(String jsonData, Class<?> clazz) {
        return JSONObject.parseObject(jsonData, clazz);
    }
    
    /**
     * 将map转化为string
     * @param m
     * @return
     */
    public static <K, V> String collectToString(Map<K, V> m) {
        String s = JSONObject.toJSONString(m);
        return s;
    }
}