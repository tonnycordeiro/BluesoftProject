package br.com.bluesoft.desafio.components.utils;

public class PageManager {

	private int resultsByPage;
	private long numberOfResults;
	private int currentPage;

	public PageManager(){
	}

	public PageManager(int resultsByPage){
		this.resultsByPage = resultsByPage; 
	}

	public PageManager(int resultsByPage, long numberOfResults, int currentPage){
		this.resultsByPage = resultsByPage;
		this.numberOfResults = numberOfResults;
		this.currentPage = currentPage;
	}
	
	public void setNumerOfresults(long numerOfresults) {
		this.numberOfResults = numerOfresults;
	}
	
	public long getNumerOfresults() {
		return numberOfResults;
	}
	
	public int getResultsByPage() {
		return resultsByPage;
	}

	public void setResultsByPage(int resultsByPage) {
		this.resultsByPage = resultsByPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public int defineFirstResult(){
		return ((this.currentPage-1)*this.resultsByPage);
	}

	public int countPages(long numberOfRows){
		double pages = (double)numberOfRows/this.resultsByPage;
		if(pages > (int)pages)
			return (int)pages + 1;
		else
			return (int)pages;
	}
	
}
