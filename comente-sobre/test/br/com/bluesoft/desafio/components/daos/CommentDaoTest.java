package br.com.bluesoft.desafio.components.daos;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.junit.Before;
import org.junit.Test;

import br.com.bluesoft.desafio.components.utils.HibernateOrder;
import br.com.bluesoft.desafio.entities.Comment;
import br.com.bluesoft.desafio.entities.Theme;
import br.com.bluesoft.desafio.entities.Visitor;
import br.com.bluesoft.desafio.examples.CommentsExample;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CommentDaoTest {
	private Comment comment;
	private List<Comment> commentsList;
	private CommentDao dao;
	private Criteria c1, c2, c3, c4;
	private Session sessionStub;
	private Transaction tx;
	
	@Before
    public void setStubs() {
        comment = new Comment();
        comment.setId(1L);
        c1 = mock(Criteria.class);
        c2 = mock(Criteria.class);
        c3 = mock(Criteria.class);
        c4 = mock(Criteria.class);
        sessionStub = mock(Session.class);
        dao = new CommentDao(sessionStub);
        tx = mock(Transaction.class);
    }

	private void createCriteriaStub(){
		when(sessionStub.createCriteria(Comment.class)).thenReturn(c1);
	}
	
	private void createCriteriaStubWithSomeCriterion(){
        when(c1.add((Criterion) any())).thenReturn(c2);
        createCriteriaStub();
	}

	private void createCriteriaStubWithFirstAndMaxAndListResultsAndWithoutCreateIt(){
		when(c3.list()).thenReturn(commentsList);
		when(c2.setMaxResults(anyInt())).thenReturn(c3);
		when(c1.setFirstResult(anyInt())).thenReturn(c2);
	}

	private void createCriteriaStubWithSomeCriterionAndAliasAndFetchMode(){
		when(c3.add((Criterion) any())).thenReturn(c4);
		when(c2.setFetchMode(anyString(),(FetchMode) any())).thenReturn(c3);
		when(c1.createAlias(anyString(), anyString())).thenReturn(c2);
		createCriteriaStub();
	}
	
    private void createDBStubWithSomeCriterionAndCommentResult() {
        when(c2.uniqueResult()).thenReturn(comment);
        createCriteriaStubWithSomeCriterion();
    }	
    
    private void createDBStubWithSomeCriterionAndNullResult() {
        when(c2.uniqueResult()).thenReturn(null);
        createCriteriaStubWithSomeCriterion();
    }    

    @Test
	public void shouldVerifyTheCommentExistis(){
    	createDBStubWithSomeCriterionAndCommentResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	assertTrue(dao.commentExists(comment));
    }
    
    @Test
	public void shouldVerifyTheCommentNotExistis(){
    	createDBStubWithSomeCriterionAndNullResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	assertFalse(dao.commentExists(comment));
    }
    
    @Test
    public void shouldLoadCommentIfCommentIsNotNull(){
    	Comment newComment;
    	createDBStubWithSomeCriterionAndCommentResult();
    	newComment = dao.loadComment(comment);
        assertEquals(newComment.getId(), comment.getId());
    }

    @Test
    public void shouldNotLoadCommentIfCommentIsNull(){
    	Comment newComment;
    	newComment = dao.loadComment(null);
        assertNull(newComment);
    }
    
    @Test
	public void shouldAddCommentIfCommentDoesntExists(){
    	Long id;
    	createDBStubWithSomeCriterionAndNullResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	when(sessionStub.save(comment)).thenReturn(comment.getId());
        id = dao.addComment(comment);
        verify(sessionStub).save(comment);
        verify(tx).commit();
        assertEquals(id, comment.getId());
	}

    @Test
	public void shouldNotAddCommentIfCommentExists(){
    	Long id;
    	createDBStubWithSomeCriterionAndCommentResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	id = dao.addComment(comment);
        verifyZeroInteractions(tx);    	
        assertNull(id);
	}
    
    @Test
	public void shouldUpdateCommentIfCommentExists(){
    	Comment newComment;
    	createDBStubWithSomeCriterionAndCommentResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	when(sessionStub.merge(comment)).thenReturn(comment);
    	newComment = dao.updateComment(comment);
        verify(sessionStub).merge(comment);
        verify(tx).commit();
        assertEquals(comment,newComment);
	}

    @Test
	public void shouldNotUpdateCommentIfCommentNotExists(){
    	Comment newComment;
    	createDBStubWithSomeCriterionAndNullResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
        newComment = dao.updateComment(comment);
        verifyZeroInteractions(tx);
        assertNull(newComment);
	}    
    
    @Test
	public void shouldDeleteCommentIfCommentExists(){
    	createDBStubWithSomeCriterionAndCommentResult();
        when(sessionStub.beginTransaction()).thenReturn(tx);        
    	dao.deleteComment(comment);
        verify(sessionStub).delete(comment);
        verify(tx).commit();
	}

    @Test
	public void shouldNotDeleteCommentIfCommentNotExists(){
    	createDBStubWithSomeCriterionAndNullResult();
    	dao.deleteComment(comment);
    	verifyZeroInteractions(tx);
	}     
    
    @Test 
    public void shouldCreateCriteriaToLoadAllComments(){
    	Criteria criteria;
        createCriteriaStub();
        criteria = dao.createCriteriaToLoadAllComments();
        assertEquals(criteria, c1);
    }
	
    @Test 
    public void shouldCreateCriteriaToLoadCommentsByVisitor(){
    	Criteria criteria;
    	Visitor visitor = new Visitor();
    	visitor.setId(1L);
    	createCriteriaStubWithSomeCriterion();
        criteria = dao.createCriteriaToLoadCommentsByVisitor(visitor);
        assertEquals(criteria, c1);
    }
	
    @Test 
    public void shouldCreateCriteriaToLoadCommentsByTheme(){
    	Criteria criteria;
    	Theme theme = new Theme();
    	theme.setId(1L);
    	createCriteriaStubWithSomeCriterion();
        criteria = dao.createCriteriaToLoadCommentsByTheme(theme);
        assertEquals(criteria, c1);
    }
	
    @Test 
    public void shouldCreateCriteriaByTheme(){
    	Criteria criteria;
    	Theme theme;
    	
    	theme = new Theme();
    	theme.setId(1L);
    	
    	createCriteriaStubWithSomeCriterion();
        criteria = dao.createCriteriaByFilters(theme, null, null, null, null, null);
        assertEquals(criteria, c1);
    }
    
    @Test 
    public void shouldCreateCriteriaByFilterVisitor(){
    	Criteria criteria;
    	Visitor visitor;
    	
    	visitor = new Visitor();
    	visitor.setId(1L);
    	
    	createCriteriaStubWithSomeCriterion();
        criteria = dao.createCriteriaByFilters(null, visitor, null, null, null, null);
        assertEquals(criteria, c1);
    }
	
    @Test 
    public void shouldCreateCriteriaByPeriodOfDate(){
    	Criteria criteria;
    	Date firstDate;
    	Date lastDate;
    	
    	firstDate = new Date();
    	lastDate = new Date();
    	
    	createCriteriaStubWithSomeCriterion();
        criteria = dao.createCriteriaByFilters(null, null, firstDate, lastDate, null, null);
        assertEquals(criteria, c1);
    }
    
    @Test 
    public void shouldNotCreateCriteriaByOnlyFirstDate(){
    	Criteria criteria;
    	Date firstDate;
    	
    	firstDate = new Date();
    	createCriteriaStub();
    	
    	createCriteriaStubWithSomeCriterion();
        criteria = dao.createCriteriaByFilters(null, null, firstDate, null, null, null);
        assertEquals(criteria, c1);
        verify(c1, never()).add((Criterion) any());
    }
    
    @Test 
    public void shouldNotCreateCriteriaByOnlyLastDate(){
    	Criteria criteria;
    	Date lastDate;
    	
    	lastDate = new Date();
    	createCriteriaStub();
    	
    	createCriteriaStubWithSomeCriterion();
        criteria = dao.createCriteriaByFilters(null, null, null, lastDate, null, null);
        assertEquals(criteria, c1);
        verify(c1, never()).add((Criterion) any());
    }

    @Test 
    public void shouldCreateCriteriaByExactTextIntoComment(){
    	Criteria criteria;
    	String exactTextIntoComment;
    	
    	exactTextIntoComment = "an filter for comment";
    	
    	createCriteriaStubWithSomeCriterion();
        criteria = dao.createCriteriaByFilters(null, null, null, null, null, exactTextIntoComment);
        assertEquals(criteria, c1);
    }

    @Test 
    public void shouldCreateCriteriaByExactTextIntoVisitorEmail(){
    	Criteria criteria;
    	String exactTextIntoVisitorEmail;
    	
    	exactTextIntoVisitorEmail = "an filter for comment";
    	
    	createCriteriaStubWithSomeCriterionAndAliasAndFetchMode();
        criteria = dao.createCriteriaByFilters(null, null, null, null, exactTextIntoVisitorEmail, null);
        assertEquals(criteria, c1);
    }

	
    @Test 
    public void shouldCreateCriteriaByAllFilters(){
    	Criteria criteria;
    	Theme theme;
    	Visitor visitor;
    	Date firstDate;
    	Date lastDate;
    	String exactTextIntoVisitorEmail;
    	String exactTextIntoComment;
    	
    	visitor = new Visitor();
    	visitor.setId(1L);
    	theme = new Theme();
    	theme.setId(1L);
    	firstDate = new Date();
    	lastDate = new Date();
    	exactTextIntoVisitorEmail = "an filter for email";
    	exactTextIntoComment = "an filter for comment";
    	
    	createCriteriaStubWithSomeCriterionAndAliasAndFetchMode();
        criteria = dao.createCriteriaByFilters(theme, visitor, firstDate, lastDate, exactTextIntoVisitorEmail, exactTextIntoComment);
        assertEquals(criteria, c1);
    }

    @Test 
    public void shouldCreateCriteriaWithoutCriterionByNullFilters(){
    	Criteria criteria;
    	createCriteriaStubWithSomeCriterionAndAliasAndFetchMode();
        criteria = dao.createCriteriaByFilters(null, null, null, null, null, null);
        assertEquals(criteria, c1);
        verify(c1, never()).add((Criterion) any());
    }
    
    @Test
    public void shouldDefineOrder(){
    	dao.defineOrder("a new field", true);
    	assertEquals("a new field", dao.getOrder().getField());
    	assertTrue(dao.getOrder().isAsc());
    }
    
    @Test
    public void shouldHaveAnOrder(){
    	HibernateOrder order;
    	order = new HibernateOrder("a field", true);

    	dao.setOrder(order);
    	
    	assertEquals(order, dao.getOrder());
    }
    
    @Test
    public void shouldLoadCommentsWithDefaulOrder(){
    	HibernateOrder order;
    	int resultsByPage = 10;
    	int firstResult = 1;
    	List<Comment> newList;

    	order = new HibernateOrder("a field", true);
    	dao.setOrder(order);
    	commentsList = CommentsExample.createListWithTwoComments();
    	createCriteriaStubWithFirstAndMaxAndListResultsAndWithoutCreateIt();
    	newList = dao.loadComments(c1, resultsByPage, firstResult);

    	assertEquals(newList,commentsList);
    	verify(c1).addOrder((Order) any());    	
    }

    @Test
    public void shouldLoadCommentsWithoutOrder(){
    	int resultsByPage = 10;
    	int firstResult = 1;
    	List<Comment> newList;
    	
    	dao.setOrder(null);
    	commentsList = CommentsExample.createListWithTwoComments();
    	createCriteriaStubWithFirstAndMaxAndListResultsAndWithoutCreateIt();
    	newList = dao.loadComments(c1, resultsByPage, firstResult);

    	assertEquals(newList,commentsList);
    	verify(c1, never()).addOrder((Order) any());
    }

    @Test
    public void shouldCountComments(){
    	long quantity;

    	when(c2.uniqueResult()).thenReturn(1L);
		when(c1.setProjection((Projection) any())).thenReturn(c2);
    	
    	quantity = dao.countComments(c1);

    	assertEquals(quantity,1L);
    }
    
}

