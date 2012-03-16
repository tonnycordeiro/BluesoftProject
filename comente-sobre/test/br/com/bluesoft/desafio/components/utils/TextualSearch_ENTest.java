package br.com.bluesoft.desafio.components.utils;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class TextualSearch_ENTest {
	/*TODO: It is waiting implement the function choosekeyWordsInTheSearchString*/
	@Test
	public void shouldChoosekeyWordsInTheSearchString() {
		List<String> keys;
		String text = "";
		TextualSearch_EN textualSearch = new TextualSearch_EN();
		keys = textualSearch.chooseKeyWordsInTheSearchString(text);
		assertNull(keys);
	}
	
}
