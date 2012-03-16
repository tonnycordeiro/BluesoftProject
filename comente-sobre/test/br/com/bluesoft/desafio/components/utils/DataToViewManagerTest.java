package br.com.bluesoft.desafio.components.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class DataToViewManagerTest {
	@Test
	public void souldNotBuildHtmlMessageForEmptyTopics(){
		String strReturned;
		List<String> topicsList = new ArrayList<String>();
		String title = "a title";

		strReturned = DataToViewManager.buildHtmlMessage(topicsList,title);
		assertEquals(strReturned, strReturned);
	}
	
	@Test
	public void souldBuildHtmlMessageForListOfTopics(){
		String strReturned;
		List<String> topicsList = new ArrayList<String>();
		topicsList.add("firts topic");
		topicsList.add("second topic");
		String title = "a title";

		strReturned = DataToViewManager.buildHtmlMessage(topicsList,title);
		assertTrue(strReturned.contains(topicsList.get(0)));
		assertTrue(strReturned.contains(topicsList.get(1)));
	}
}
