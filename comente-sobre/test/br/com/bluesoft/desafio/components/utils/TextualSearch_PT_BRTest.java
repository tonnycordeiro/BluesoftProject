package br.com.bluesoft.desafio.components.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

public class TextualSearch_PT_BRTest {
	
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
    private List<String> allPluralArticles = new ArrayList<String>(  
		  Arrays.asList("OS","AS","UNS","UMAS")
		  );
    
	@Test
	public void shouldOnlyContainUpperCaseKeys(){
		String key, text;
		List<String> keys;
		TextualSearch_PT_BR textualSearch = new TextualSearch_PT_BR();

		text = "a COISA o nome";
		keys = textualSearch.chooseKeyWordsInTheSearchString(text);
		text = "A COISA O NOME";
		keys = textualSearch.chooseKeyWordsInTheSearchString(text);

		Iterator<String> i = keys.iterator();
		while(i.hasNext()){
			key = i.next().toUpperCase();
			assertTrue(keys.contains(key));			
		}
	}	
    
    private void shouldAssertFalseForValuesInList(List<String> forbiddenSymbols, List<String> allSymbols){
		Iterator<String> i = forbiddenSymbols.iterator();
		while(i.hasNext()){
			assertFalse(allSymbols.contains(i.next()));			
		}
	}
	
	private String buildStringWithSymbols(List<String> forbiddenSymbols,String betweenTwoSymbolsStr){
		String text = "";
		Iterator<String> i = forbiddenSymbols.iterator();
		while(i.hasNext()){
			text += i.next() + " " + betweenTwoSymbolsStr + " ";			
		}
		return text;
	}
	
	@Test
	public void shouldchoosekeyWordsInTheSearchStringWithAllSingularArticles(){
		List<String> keys;
		TextualSearch_PT_BR textualSearch = new TextualSearch_PT_BR(); 		
		
		String text = buildStringWithSymbols(allSingularArticles,"nome");
		keys = textualSearch.chooseKeyWordsInTheSearchString(text);
		
		assertTrue(keys.contains("NOME"));
		shouldAssertFalseForValuesInList(allSingularArticles, keys);
	}
	
	@Test
	public void shouldchoosekeyWordsInTheSearchStringWithAllPluralArticles(){
		List<String> keys;
		TextualSearch_PT_BR textualSearch = new TextualSearch_PT_BR(); 		
		
		String text = buildStringWithSymbols(allPluralArticles,"nome");
		keys = textualSearch.chooseKeyWordsInTheSearchString(text);
		
		assertTrue(keys.contains("NOME"));
		shouldAssertFalseForValuesInList(allPluralArticles, keys);

	}
	
	@Test
	public void shouldchoosekeyWordsInTheSearchStringWithPopConjunctions(){
		List<String> keys;
		TextualSearch_PT_BR textualSearch = new TextualSearch_PT_BR(); 		
		
		String text = buildStringWithSymbols(popConjunctions,"nome");
		keys = textualSearch.chooseKeyWordsInTheSearchString(text);
		
		assertTrue(keys.contains("NOME"));
		shouldAssertFalseForValuesInList(popConjunctions, keys);

	}
	
	@Test
	public void shouldchoosekeyWordsInTheSearchStringWithAllSingularContractions(){
		List<String> keys;
		TextualSearch_PT_BR textualSearch = new TextualSearch_PT_BR(); 		
		
		String text = buildStringWithSymbols(allSingularContractions,"nome");
		keys = textualSearch.chooseKeyWordsInTheSearchString(text);
		
		assertTrue(keys.contains("NOME"));
		shouldAssertFalseForValuesInList(allSingularContractions, keys);		
	}
	
	@Test
	public void shouldchoosekeyWordsInTheSearchStringWithPopPrepositions(){
		List<String> keys;
		TextualSearch_PT_BR textualSearch = new TextualSearch_PT_BR(); 		
		
		String text = buildStringWithSymbols(popPrepositions,"nome");
		keys = textualSearch.chooseKeyWordsInTheSearchString(text);
		
		assertTrue(keys.contains("NOME"));
		shouldAssertFalseForValuesInList(popPrepositions, keys);		
	}
	
	@Test
	public void shouldContainsThreeKeys(){
		List<String> keys;
		TextualSearch_PT_BR textualSearch = new TextualSearch_PT_BR(); 		
		
		String text = "a palavra1 do palavra2 por palavra3";
		keys = textualSearch.chooseKeyWordsInTheSearchString(text);
		
		assertTrue(keys.contains("PALAVRA1"));
		assertTrue(keys.contains("PALAVRA2"));
		assertTrue(keys.contains("PALAVRA3"));
		assertTrue(keys.size() == 3);
	}	
	
	@Test
	public void shouldContainsThreeKeysWithoutSpace(){
		List<String> keys;
		TextualSearch_PT_BR textualSearch = new TextualSearch_PT_BR(); 		
		
		String text = "    a       palavra1 do     palavra2 por palavra3    ";
		keys = textualSearch.chooseKeyWordsInTheSearchString(text);
		
		assertTrue(keys.contains("PALAVRA1"));
		assertTrue(keys.contains("PALAVRA2"));
		assertTrue(keys.contains("PALAVRA3"));
		assertTrue(keys.size() == 3);
	}		
	
	@Test
	public void shouldNotContainsKeys(){
		List<String> keys;
		TextualSearch_PT_BR textualSearch = new TextualSearch_PT_BR(); 		
		
		String text = "      ";
		keys = textualSearch.chooseKeyWordsInTheSearchString(text);
		
		assertTrue(keys.size() == 0);
	}	
	
	
}
