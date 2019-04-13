package com.share_will.mobile.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {
public static String readInputStream(InputStream is) {
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		int length=0;
		byte [] buffer =new byte[1024];
		try {
			while ((length=is.read(buffer))!=-1) {
				baos.write(buffer,0,length);
			}
			is.close();
			baos.close();
			byte [] result=baos.toByteArray();
			//试着解析result里的字符
			String temp=new String(result);
			if (temp.contains("utf-8")) {
				return temp;
			}else if(temp.contains("gb2312")){
				return new String(result,"gb2312");
			}
			else if(temp.contains("gbk")){
				return new String(result,"gbk");
			}else {
				return temp;
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "获取失败";
		}
		
	}
}
