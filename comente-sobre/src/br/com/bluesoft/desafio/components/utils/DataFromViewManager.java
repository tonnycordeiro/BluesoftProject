package br.com.bluesoft.desafio.components.utils;

import java.io.UnsupportedEncodingException;

public class DataFromViewManager {

	public static String unescapeStringReturnedFromJavascriptMethodEscape(String str){
		try {
			return java.net.URLDecoder.decode(str,"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
}
