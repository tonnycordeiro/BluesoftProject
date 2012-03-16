package br.com.bluesoft.desafio.components.utils;

import java.util.Iterator;
import java.util.List;

public class DataToViewManager {

	public static String buildHtmlMessage(List<String> topicsList, String title){
    	String errorPhrase = new String();
    	Iterator<String> iterator;
    	
    	if(topicsList.isEmpty())
    		return null;
    	errorPhrase = title;
    	errorPhrase = errorPhrase.concat("<ul>");
    	
    	iterator = topicsList.iterator();
    	while(iterator.hasNext())
    		errorPhrase = errorPhrase.concat(new String("<li>").concat((String)iterator.next()).concat("</li>"));
    	
    	errorPhrase = errorPhrase.concat("</ul>");
    	
    	return errorPhrase;
    }

}
