package br.com.bluesoft.desafio.components.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class DataFromViewManagerTest {
	@Test
	public void shouldUnescapeStringReturnedFromJavascriptMethodEscape(){
		String returnedStr;
		String escapedString = "%20%21%23%24%25%26%27%28%29*%2B%2C-.%2F0123456789%3A%3B%3C%3D%3E%3F%40ABCDEFGHIJKLMNOPQRSTUVWXYZ%5B%5D%5E_%60" +
				"abcdefghijklmnopqrstuvwxyz%7B%7C%7D%7E%A1%A2%A3%A5%7C%A7%A8%A9%AA%AB%AC%AF%AE%AF%B0%B1%B2%B3%B4%B5%B6%B7%B8%B9%BA%BB%BC%BD%BE" +
				"%BF%C0%C1%C2%C3%C4%C5%C6%C7%C8%C9%CA%CB%CC%CD%CE%CF%D0%D1%D2%D3%D4%D5%D6%D8%D9%DA%DB%DC%DD%DE%DF%E0%E1%E2%E3%E4%E5%E6%E7%E8%E9" +
				"%EA%EB%EC%ED%EE%EF%F0%F1%F2%F3%F4%F5%F6%F7%F8%F9%FA%FB%FC%FD%FE%FF%22%5C";
		String unescapedString = " !#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~¡¢£¥|§¨©" +
				"ª«¬¯®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏĞÑÒÓÔÕÖØÙÚÛÜİŞßàáâãäåæçèéêëìíîïğñòóôõö÷øùúûüışÿ" + '"' + "\\";
		
		returnedStr = DataFromViewManager.unescapeStringReturnedFromJavascriptMethodEscape(escapedString);
		assertEquals(unescapedString, returnedStr);
		
	}
}
