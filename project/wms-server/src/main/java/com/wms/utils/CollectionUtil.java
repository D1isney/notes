package com.wms.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

public class CollectionUtil {


    public static <T,K> void mapEntry(Map<T,K> map, Predicate<Iterator<Map.Entry<T, K>>> predicate){
        Iterator<Map.Entry<T, K>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            boolean test = predicate.test(iterator);
            if(test) break;
        }
    }

}
