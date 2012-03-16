package br.com.bluesoft.desafio.components.daos;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.junit.Before;
import org.junit.Test;

import br.com.bluesoft.desafio.components.utils.HibernateOrder;
import br.com.bluesoft.desafio.entities.Theme;
import br.com.bluesoft.desafio.examples.ThemesExample;

public class ThemeDaoTest {
	private Theme theme;
	private List<Theme> themesList;
	private ThemeDao dao;
	private Criteria c1, c2, c3;
	private Session sessionStub;
	private Transaction tx;
	
	@Before
    public void setStubs() {
        theme = new Theme();
        theme.setId(1L);
        theme.setName("a name");
        theme.setUrl("an url");
        c1 = mock(Criteria.class);
        c2 = mock(Criteria.class);
        c3 = mock(Criteria.class);
        sessionStub = mock(Session.class);
        dao = new ThemeDao(sessionStub);
        tx = mock(Transaction.class);
    }

	private void createCriteriaStub(){
		when(sessionStub.createCriteria(Theme.class)).thenReturn(c1);
	}
	
	private void createCriteriaStubWithSomeCriterion(){
        when(c1.add((Criterion) any())).thenReturn(c2);
        createCriteriaStub();
	}

	private void createCriteriaStubWithOrderAndSomeCriterion(){
		when(c3.list()).thenReturn(themesList);
        when(c2.add((Criterion) any())).thenReturn(c3);
        when(c1.addOrder((Order) any())).thenReturn(c2);
        createCriteriaStub();
	}

	private void createCriteriaStubWithSomeCriterionAndThemesListResult(){
		when(c2.list()).thenReturn(themesList);
		createCriteriaStubWithSomeCriterion();
	}

    private void createDBStubWithSomeCriterionAndThemeResult() {
        when(c2.uniqueResult()).thenReturn(theme);
        createCriteriaStubWithSomeCriterion();
    }	
    
    private void createDBStubWithSomeCriterionAndNullResult() {
        when(c2.uniqueResult()).thenReturn(null);
        createCriteriaStubWithSomeCriterion();
    }    

    @Test
	public void shouldVerifyTheThemeExistis(){
    	createDBStubWithSomeCriterionAndThemeResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	assertTrue(dao.themeExists(theme));
    }
    
    @Test
	public void shouldVerifyTheThemeNotExistis(){
    	createDBStubWithSomeCriterionAndNullResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	assertFalse(dao.themeExists(theme));
    }
    
    @Test
    public void shouldLoadThemeByURLIfThemeIsNotNull(){
    	Theme newTheme;
    	createDBStubWithSomeCriterionAndThemeResult();
    	newTheme = dao.loadThemeByURL(theme.getUrl());
        assertEquals(newTheme.getId(), theme.getId());
    }
    
    @Test
    public void shouldLoadThemeIfThemeIsNotNull(){
    	Theme newTheme;
    	createDBStubWithSomeCriterionAndThemeResult();
    	newTheme = dao.loadTheme(theme);
        assertEquals(newTheme.getId(), theme.getId());
    }

    @Test
    public void shouldNotLoadThemeIfThemeIsNull(){
    	Theme newTheme;
    	newTheme = dao.loadTheme(null);
        assertNull(newTheme);
    }

    @Test
    public void shouldNotLoadThemeByURLIfThemeIsNull(){
    	Theme newTheme;
    	newTheme = dao.loadThemeByURL(null);
        assertNull(newTheme);
    }
    
    @Test
	public void shouldAddThemeIfThemeDoesntExists(){
    	Long id;
    	createDBStubWithSomeCriterionAndNullResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	when(sessionStub.save(theme)).thenReturn(theme.getId());
        id = dao.addTheme(theme);
        verify(sessionStub).save(theme);
        verify(tx).commit();
        assertEquals(id, theme.getId());
	}

    @Test
	public void shouldNotAddThemeIfThemeExists(){
    	Long id;
    	createDBStubWithSomeCriterionAndThemeResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	id = dao.addTheme(theme);
        verifyZeroInteractions(tx);    	
        assertNull(id);
	}
    
    @Test
	public void shouldUpdateThemeIfThemeExists(){
    	Theme newTheme;
    	createDBStubWithSomeCriterionAndThemeResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	when(sessionStub.merge(theme)).thenReturn(theme);
    	newTheme = dao.updateTheme(theme);
        verify(sessionStub).merge(theme);
        verify(tx).commit();
        assertEquals(theme,newTheme);
	}

    @Test
	public void shouldNotUpdateThemeIfThemeNotExists(){
    	Theme newTheme;
    	createDBStubWithSomeCriterionAndNullResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
        newTheme = dao.updateTheme(theme);
        verifyZeroInteractions(tx);
        assertNull(newTheme);
	}    
    
    @Test
	public void shouldDeleteThemeIfThemeExists(){
    	createDBStubWithSomeCriterionAndThemeResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	dao.deleteTheme(theme);
        verify(sessionStub).delete(theme);
        verify(tx).commit();
	}

    @Test
	public void shouldNotDeleteThemeIfThemeNotExists(){
    	createDBStubWithSomeCriterionAndNullResult();
    	dao.deleteTheme(theme);
    	verifyZeroInteractions(tx);
	}     
    
    @Test
    public void shouldHaveAnOrder(){
    	HibernateOrder order;
    	order = new HibernateOrder("a field", true);

    	dao.setOrder(order);
    	
    	assertEquals(order, dao.getOrder());
    }    
    
    @Test
    public void shouldDefineOrder(){
    	dao.defineOrder("a new field", true);
    	assertEquals("a new field", dao.getOrder().getField());
    	assertTrue(dao.getOrder().isAsc());
    }     
    
    @Test
    public void shouldLoadThemesWithDefaulOrder(){
    	HibernateOrder order;
    	List<Theme> newList;
    	List<String> stringsList = new ArrayList<String>();
    	stringsList.add("key 1");
    	stringsList.add("key 2");

    	order = new HibernateOrder("a field", true);
    	dao.setOrder(order);
    	themesList = ThemesExample.createListWithTwoThemes();
    	createCriteriaStubWithOrderAndSomeCriterion();
    	newList = dao.searchThemeWithKeyWords(stringsList);

    	assertEquals(newList,themesList);
    	verify(c1).addOrder((Order) any());    	
    }

    @Test
    public void shouldLoadThemesWithoutOrder(){
    	List<Theme> newList;
    	List<String> stringsList = new ArrayList<String>();
    	stringsList.add("key 1");
    	stringsList.add("key 2");
    	    	
    	dao.setOrder(null);
    	themesList = ThemesExample.createListWithTwoThemes();
    	createCriteriaStubWithSomeCriterionAndThemesListResult();
    	newList = dao.searchThemeWithKeyWords(stringsList);

    	assertEquals(newList,themesList);
    	verify(c1, never()).addOrder((Order) any());
    }

}

