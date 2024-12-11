package com.wms.utils;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class ObjectUtil {
	
	/**对象保存成文件*/
	public static void writeObjectToFile(Object obj,String path) throws IOException{
		File file =new File(path);
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		FileOutputStream out = new FileOutputStream(file);
		ObjectOutputStream objOut=new ObjectOutputStream(out);
		objOut.writeObject(obj);
		objOut.flush();
		objOut.close();
		out.close();
	}
	
	
	/**对象保存成文件*/
	public static void writeObjectToFile(Object obj,OutputStream out) throws IOException{
		ObjectOutputStream objOut=new ObjectOutputStream(out);
		objOut.writeObject(obj);
		objOut.flush();
		objOut.close();
	}
	
	/**对象转换成文件*/
	public static byte[] writeObjectToByte(Object obj) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream objOut=new ObjectOutputStream(out);
		objOut.writeObject(obj);
		objOut.flush();
		objOut.close();
		return out.toByteArray();
	}

	public static Object readObjectFromFile(String path) throws IOException, ClassNotFoundException{
		Object temp=null;
		File file =new File(path);
		FileInputStream in = new FileInputStream(file);
		ObjectInputStream objIn=new ObjectInputStream(in);
		temp=objIn.readObject();
		objIn.close();
		return temp;
	}
	
	public static Object readObjectFromFile(InputStream is) throws IOException, ClassNotFoundException{
		Object temp=null;
		ObjectInputStream objIn=new ObjectInputStream(is);
		temp=objIn.readObject();
		objIn.close();
		return temp;
	}
	
	/**byte还原成对象*/
	public static Object readObjectFromByte(byte[] data) throws IOException, ClassNotFoundException{
		Object temp=null;
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream objIn=new ObjectInputStream(in);
		temp=objIn.readObject();
		objIn.close();
		return temp;
	}
	
	
	public static <T> T objectToType(Object obj){
		return (T) obj;
	}
	
	public static <T> List<T> objectToType(List<Object> objs){
		List<T> result = new ArrayList<>();
		for(Object obj : objs) {
			result.add((T) objectToType(obj));
		}
		return result;
	}
	
	public static List<Object> typeToObject(List<? extends Object> types){
		return new ArrayList<>(types);
	}
	
	public static <T> T[] objectToType(Object[] objs,Class<T> type){
		List<Object> asList = Arrays.asList(objs);
		List<T> typeList = objectToType(asList);
		return typeList.toArray((T[]) Array.newInstance(type, typeList.size()));
	}

	public static void writerString2File(String strData,String path) throws IOException {
		File file =new File(path);
		writerString2File(file,strData);
	}

	public static void writerString2File(File file, String strData) throws IOException {
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try (FileOutputStream out = new FileOutputStream(file); BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));) {
			bufferedWriter.write(strData);
		}
	}


	public static String readString(String path) throws IOException {
		File file =new File(path);
		return readString(file);
	}

	public static String readString(File file) throws IOException {
		String temp=null;
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file),StandardCharsets.UTF_8));
		StringBuilder stringBuilder = new StringBuilder();
		try {
			while ((temp = in.readLine()) != null){
				stringBuilder.append(temp);
			}
		}finally {
			in.close();
		}
		String s = stringBuilder.toString();
		if(StringUtil.NULL_STR.equalsIgnoreCase(s)){
			return null;
		}
		return s;
	}

}