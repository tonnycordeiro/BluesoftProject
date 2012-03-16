package br.com.bluesoft.desafio.entities;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import br.com.bluesoft.desafio.examples.CommentsExample;


public class VisitorTest {
	private Visitor visitor;
	
	private Long id;
	private String name;
	private String email;
	
	@Before
	public void createVitorWithBasicValues(){
		visitor = new Visitor();
		
		id = 1L;
		name = "a visitor name";
		email = "a visitor email";
		
		visitor.setId(id);
		visitor.setEmail(email);
		visitor.setName(name);
		visitor.setComments(new ArrayList<Comment>());
	}
	
	@Test
	public void shouldHaveAnId(){
		assertEquals(id, visitor.getId());
	}

	@Test
	public void shouldHaveAName(){
		assertEquals(name, visitor.getName());
	}
	
	@Test
	public void shouldHaveAnEmail(){
		assertEquals(email, visitor.getEmail());
	}

    @Test
    public void shouldConvertToStringWithMainAttributes(){
    	String strVisitor = visitor.toString();
    	assertTrue(strVisitor.contains(id.toString()));
    	assertTrue(strVisitor.contains(name));
    	assertTrue(strVisitor.contains(email));
    }
	
	@Test
	public void shouldCreateAVisitorWithEmail(){
		Visitor visitorWithEmail = new Visitor(email);
		assertEquals(email, visitorWithEmail.getEmail());
	}
	
    private Comment[] addListWithTwoCommentsInTheVisitor(){
    	Comment[] comments = CommentsExample.createArrayWithTwoComments(); 

    	visitor.addComment(comments[0]);
    	visitor.addComment(comments[1]);
    	
    	return comments;
    }

    private Comment[] addListWithOneCommentInTheVisitor(){
    	Comment[] comments = new Comment[1];
    	comments[0] = CommentsExample.createComment("a description 1",1L,new Date());

    	visitor.addComment(comments[0]);
    	
    	return comments;
    }
	
    @Test
    public void shouldHaveAListOfTwoComments(){
    	Comment[] comments;
    	comments = addListWithTwoCommentsInTheVisitor();
    	
    	assertTrue(visitor.getComments().contains(comments[0]));
    	assertTrue(visitor.getComments().contains(comments[1]));
        assertEquals(2, visitor.getComments().size());
    }

    @Test
    public void shouldNotHaveAListOfComments(){
        assertEquals(0, visitor.getComments().size());
    }
    
    @Test
    public void shoudAddOneCommentIfThemeHaveZeroComments(){
    	Comment comments[];
    	comments = addListWithOneCommentInTheVisitor();
    	
    	assertTrue(visitor.getComments().contains(comments[0]));
    	assertEquals(1, visitor.getComments().size());
    }    

    @Test
    public void shoudAddOneCommentIfThemeHaveOneComment(){
    	Comment comment;
    	
    	addListWithOneCommentInTheVisitor();    	
    	comment = CommentsExample.createComment("a description 2",2L,new Date());
    	visitor.addComment(comment);

    	assertTrue(visitor.getComments().contains(comment));
    	assertEquals(2, visitor.getComments().size());
    }    

    
    @Test
    public void shouldRemoveOneCommentIfThemeHaveTwoComments(){
    	Comment[] comments;
    	comments = addListWithTwoCommentsInTheVisitor();
    	
    	visitor.removeComment(comments[0]);
    	assertFalse(visitor.getComments().contains(comments[0]));
    	assertTrue(visitor.getComments().contains(comments[1]));
        assertEquals(1, visitor.getComments().size());
    }
    
    @Test
    public void shouldRemoveOneCommentIfThemeHaveOneComment(){
    	Comment[] comments;
    	comments = addListWithOneCommentInTheVisitor();
    	
    	visitor.removeComment(comments[0]);
    	assertFalse(visitor.getComments().contains(comments[0]));
        assertEquals(0, visitor.getComments().size());
    }
    
    @Test
    public void shouldNotRemoveOneCommentIfThemeHaveZeroComments(){
    	Comment comment = CommentsExample.createComment("a description",1L,new Date());

    	visitor.removeComment(comment);
    	assertFalse(visitor.getComments().contains(comment));
        assertEquals(0, visitor.getComments().size());
    }
	
	
}
