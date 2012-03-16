package br.com.bluesoft.desafio.components.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PageManagerTest {
	private PageManager page;
	private int currentPage;
	private int numberOfResults;
	private int resultsByPage;
	
	@Before
	public void setUp(){
		page = new PageManager();
		currentPage = 1;
		numberOfResults = 10;
		resultsByPage = 10;
		
		page.setCurrentPage(currentPage);
		page.setNumerOfresults(numberOfResults);
		page.setResultsByPage(resultsByPage);
	}
	
	@Test
	public void shouldHaveACurrentPage(){
		assertTrue(page.getCurrentPage() == currentPage);
	}
	
	@Test
	public void shouldHaveANumberOfResults(){
		assertTrue(page.getNumerOfresults() == numberOfResults);
	}
	
	@Test
	public void shouldHaveResultsByPage(){
		assertTrue(page.getResultsByPage() == resultsByPage);
	}
	
	@Test
	public void shouldDefineFirstResultInTheSameFirstPage(){
		int firstResult;
		page.setResultsByPage(5);
		page.setCurrentPage(1);
		firstResult = page.defineFirstResult();
		assertTrue(firstResult == 0);
	}

	@Test
	public void shouldDefineFirstResultInTheSameSecondPage(){
		int firstResult;
		page.setResultsByPage(5);
		page.setCurrentPage(2);
		firstResult = page.defineFirstResult();
		assertTrue(firstResult == 5);
	}
	
	@Test
	public void shouldCountPagesForRowsNumberLessThenResultsByPage(){
		int numberOfPages, numberOfRows;
		
		numberOfRows= 4;
		page.setResultsByPage(5);
		
		numberOfPages = page.countPages(numberOfRows);
		assertTrue(numberOfPages == 1);
	}	

	
	@Test
	public void shouldCountPagesForRowsNumberEqualTheResultsByPage(){
		int numberOfPages, numberOfRows;
		
		numberOfRows= 5;
		page.setResultsByPage(5);
		
		numberOfPages = page.countPages(numberOfRows);
		assertTrue(numberOfPages == 1);
	}	

	@Test
	public void shouldCountPagesForRowsNumberMoreThenResultsByPageAndWithoutMaximunItensInTheLastPage(){
		int numberOfPages, numberOfRows;
		
		numberOfRows= 12;
		page.setResultsByPage(5);
		
		numberOfPages = page.countPages(numberOfRows);
		assertTrue(numberOfPages == 3);
	}	

	@Test
	public void shouldCountPagesForRowsNumberMoreThenResultsByPageAndWithMaximunItensInTheLastPage(){
		int numberOfPages;
		long numberOfRows;
		
		numberOfRows= 10;
		page.setResultsByPage(5);
		
		numberOfPages = page.countPages(numberOfRows);
		assertTrue(numberOfPages == 2);
	}	
	

}
