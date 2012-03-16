package br.com.bluesoft.desafio.components.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextualSearch_EN implements TextualSearch{

    public TextualSearch_EN(){
    	
    }	
	
    @SuppressWarnings("unused")
	private List<String> popConjunctionsAndPrepositions = new ArrayList<String>(
		  Arrays.asList("OF,TO,AND,IN,THAT,FOR,ON,WITH,AS,AT,FROM,OR,BY,BUT")
		  );
    @SuppressWarnings("unused")
	private List<String> allArticles = new ArrayList<String>(  
  		  Arrays.asList("THE","A","AN")
  		  );	
	
    /*TODO: implement the function choosekeyWordsInTheSearchString*/
    @Override
	public List<String> chooseKeyWordsInTheSearchString(String text) {
		return null;
	}
	
}
