package br.com.bluesoft.desafio.components.daos;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Test;

import br.com.bluesoft.desafio.entities.Visitor;


public class VisitorDaoTest {
	private Visitor visitor;
	private VisitorDao dao;
	private Criteria c1, c2;
	private Session sessionStub;
	private Transaction tx;
	
	@Before
    public void setStubs() {
        visitor = new Visitor();
        visitor.setId(1L);
        visitor.setName("a name");
        visitor.setEmail("an email");
        c1 = mock(Criteria.class);
        c2 = mock(Criteria.class);
        sessionStub = mock(Session.class);
        dao = new VisitorDao(sessionStub);
        tx = mock(Transaction.class);
    }

	private void createCriteriaStub(){
		when(sessionStub.createCriteria(Visitor.class)).thenReturn(c1);
	}
	
	private void createCriteriaStubWithSomeCriterion(){
        when(c1.add((Criterion) any())).thenReturn(c2);
        createCriteriaStub();
	}

    private void createDBStubWithSomeCriterionAndVisitorResult() {
        when(c2.uniqueResult()).thenReturn(visitor);
        createCriteriaStubWithSomeCriterion();
    }	
    
    private void createDBStubWithSomeCriterionAndNullResult() {
        when(c2.uniqueResult()).thenReturn(null);
        createCriteriaStubWithSomeCriterion();
    }    

    @Test
	public void shouldVerifyTheVisitorExistis(){
    	createDBStubWithSomeCriterionAndVisitorResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	assertTrue(dao.visitorExists(visitor));
    }
    
    @Test
	public void shouldVerifyTheVisitorNotExistis(){
    	createDBStubWithSomeCriterionAndNullResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	assertFalse(dao.visitorExists(visitor));
    }
    
    @Test
    public void shouldLoadVisitorByEmailIfEmailIsNotNull(){
    	Visitor newVisitor;
    	createDBStubWithSomeCriterionAndVisitorResult();
    	newVisitor = dao.loadVisitorByEmail(visitor.getEmail());
        assertEquals(newVisitor.getId(), visitor.getId());
    }

    @Test
    public void shouldLoadVisitorIfVisitorIsNotNull(){
    	Visitor newVisitor;
    	createDBStubWithSomeCriterionAndVisitorResult();
    	newVisitor = dao.loadVisitor(visitor);
        assertEquals(newVisitor.getId(), visitor.getId());
    }

    @Test
    public void shouldNotLoadVisitorIfVisitorIsNull(){
    	Visitor newVisitor;
    	newVisitor = dao.loadVisitor(null);
        assertNull(newVisitor);
    }

    @Test
    public void shouldNotLoadVisitorByEmailIfEmailIsNull(){
    	Visitor newVisitor;
    	newVisitor = dao.loadVisitorByEmail(null);
        assertNull(newVisitor);
    }
    
    @Test
	public void shouldAddVisitorIfVisitorDoesntExists(){
    	Long id;
    	createDBStubWithSomeCriterionAndNullResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	when(sessionStub.save(visitor)).thenReturn(visitor.getId());
        id = dao.addVisitor(visitor);
        verify(sessionStub).save(visitor);
        verify(tx).commit();
        assertEquals(id, visitor.getId());
	}

    @Test
	public void shouldNotAddVisitorIfVisitorExists(){
    	Long id;
    	createDBStubWithSomeCriterionAndVisitorResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	id = dao.addVisitor(visitor);
        verifyZeroInteractions(tx);    	
        assertNull(id);
	}
    
    @Test
	public void shouldUpdateVisitorIfVisitorExists(){
    	Visitor newVisitor;
    	createDBStubWithSomeCriterionAndVisitorResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	when(sessionStub.merge(visitor)).thenReturn(visitor);
    	newVisitor = dao.updateVisitor(visitor);
        verify(sessionStub).merge(visitor);
        verify(tx).commit();
        assertEquals(visitor,newVisitor);
	}

    @Test
	public void shouldNotUpdateVisitorIfVisitorNotExists(){
    	Visitor newVisitor;
    	createDBStubWithSomeCriterionAndNullResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
        newVisitor = dao.updateVisitor(visitor);
        verifyZeroInteractions(tx);
        assertNull(newVisitor);
	}    
    
    @Test
	public void shouldDeleteVisitorIfVisitorExists(){
    	createDBStubWithSomeCriterionAndVisitorResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	dao.deleteVisitor(visitor);
        verify(sessionStub).delete(visitor);
        verify(tx).commit();
	}

    @Test
	public void shouldNotDeleteVisitorIfVisitorNotExists(){
    	createDBStubWithSomeCriterionAndNullResult();
    	dao.deleteVisitor(visitor);
    	verifyZeroInteractions(tx);
	}     
	
}
