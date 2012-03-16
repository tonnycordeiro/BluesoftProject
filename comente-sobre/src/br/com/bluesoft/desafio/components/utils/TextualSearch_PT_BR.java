package br.com.bluesoft.desafio.components.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TextualSearch_PT_BR implements TextualSearch{

    private static final Integer maxLengthOfContractions = 4; 
	
    private List<String> popConjunctions = new ArrayList<String>(
		  Arrays.asList("E","OU","MAS","QUE","SE")
		  );
    private List<String> popPrepositions = new ArrayList<String>(  
  		  Arrays.asList("DE","EM","POR","COM","PARA","SEM")
  		  );
    private List<String> allSingularContractions = new ArrayList<String>(  
  		  Arrays.asList("DO","DA","NO","NA","AO","À","PELO","PELA","DUM","DUMA","NUM","NUMA")
  		  );
    private List<String> allSingularArticles = new ArrayList<String>(  
  		  Arrays.asList("O","A","UM","UMA")
  		  );

    public TextualSearch_PT_BR(){
    	
    }
    
    /*P.S: A lot of search engines eliminate the prepositions and articles of your searches*/
    @Override
	public List<String> chooseKeyWordsInTheSearchString(String text) {
    	boolean eliminatedWord = false;
    	String candidateToElimination, key;
    	List<String> words;
    	List<String> keys;
    	Iterator<String> iterator;

    	keys = new ArrayList<String>();
    	text = text.trim().toUpperCase();
    	if(text == "")
    		return keys;
    	words = Arrays.asList(text.split(" "));
    	iterator = words.iterator();
    	
    	while(iterator.hasNext()){
    		candidateToElimination = (String)iterator.next().trim();
    		if(popPrepositions.contains(candidateToElimination) || popConjunctions.contains(candidateToElimination))
				eliminatedWord = true;
    		else{
    			candidateToElimination = convertUpperCaseArticlesOrContractionsToSingular(candidateToElimination);
    			if(allSingularArticles.contains(candidateToElimination) || allSingularContractions.contains(candidateToElimination))
    				eliminatedWord = true;
    		}
    		if(eliminatedWord && iterator.hasNext()){
    			if(!keys.contains( key = iterator.next().trim() ) && !key.isEmpty())
    				keys.add(key);/*so the next is a key*/
    		}
    		else
    			if(!keys.contains(candidateToElimination) && !candidateToElimination.isEmpty())
    				keys.add(candidateToElimination);
    	}
    	
    	return keys;
	}
    
    private boolean isSmallerThanMaxLengthOfContractions(String word){
    	return word.length() <= maxLengthOfContractions;
    }
    
    private String convertUpperCaseArticlesOrContractionsToSingular(String word){
    	/*Consider the only valid rules for this grammatical classes*/
    	Integer length = word.length();
    	
    	if(length >= 2 && isSmallerThanMaxLengthOfContractions(word))
    		if(word.charAt(length-1) == 'S'){
    			if(word.charAt(length-2) == 'N'){
    				word = word.substring(0,length-2);
        			word.concat("M");    				
    			}
    			else{
    				word = word.substring(0,length-1);
    			}
    		}
		
    	return word;
    }    

}
