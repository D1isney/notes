package com.wms.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HttpUtil {
	
	
	public static void setResponse(HttpServletResponse response , String userAgent,String name) throws UnsupportedEncodingException {
		if (userAgent.indexOf("MSIE") > 0) {// IE浏览器
			name = new String(name.getBytes("UTF-8"), "ISO-8859-1");
			} else {
				name = URLEncoder.encode(name, "UTF-8");
			}
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName="+name);
	}

	public static void responseJSON(HttpServletResponse response,Object object) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "application/json");
		response.getWriter().print(FastJsonUtils.convertObjectToJSON(object));
		response.getWriter().flush();
	}

	public static void responseJSON0(HttpServletResponse response,Object object) {
		try {
			responseJSON(response,object);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
