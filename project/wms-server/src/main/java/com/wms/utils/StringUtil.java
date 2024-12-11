package com.wms.utils;


import java.util.Collection;



public class StringUtil {
	public static final String EMPTY = "";
	public static final String NULL_STR = "null";

	public static boolean isEmpty(Object object) {
		if(object == null) {
			return true;
		}else if(object instanceof String) {
			String str = object.toString();
			if(EMPTY.equals(str.trim())) {
				return true;
			}
		}else if(object instanceof Collection) {
			Collection collection = (Collection)object;
			if(collection.isEmpty()) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
}
