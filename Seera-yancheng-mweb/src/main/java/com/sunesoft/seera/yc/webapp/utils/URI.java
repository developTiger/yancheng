package com.sunesoft.seera.yc.webapp.utils;

import java.io.UnsupportedEncodingException;

/**
 * rurl地址转换，防止rurl中的参数被当成请求参数
 * @author zhouz
 *
 */
public class URI {
	
	/**
	 * 转换
	 * @param url
	 * @return
	 */
	public static String enURI(String url){
		try {
			url = java.net.URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	/**
	 * 反转
	 * @param url
	 * @return
	 */
	public static String deURI(String url){
		try {
			url = java.net.URLDecoder.decode(url,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
}
