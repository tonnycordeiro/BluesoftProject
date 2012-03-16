package br.com.bluesoft.desafio.entities;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class CommentTest {
	
	private Comment comment;
	private Long id;
	private String description;
	private Date lastUpdate;

	@Before
    public void createCommentWithBasicValues() {
    	comment = new Comment();

    	description = "a comment description";
        id = 1L;
        lastUpdate = new Date();
        
        comment.setId(id);
        comment.setDescription(description);
        comment.setLastUpdate(lastUpdate);
    }
    
    @Test
    public void shouldHaveAnId(){
    	assertEquals(id, comment.getId());
    }
    
    @Test
    public void shouldHaveADescription(){
    	assertEquals(description, comment.getDescription());
    }
    
    @Test
    public void shouldHaveALastUpdate(){
        lastUpdate = new Date();
        comment.setLastUpdate(lastUpdate);
    	assertEquals(lastUpdate, comment.getLastUpdate());
    }

    @Test
    public void shouldConvertToStringWithMainAttributes(){
    	String strComment = comment.toString();
    	assertTrue(strComment.contains(id.toString()));
    	assertTrue(strComment.contains(description));
    	assertTrue(strComment.contains(lastUpdate.toString()));
    }
    
    private Visitor createAVisitor(Long id, String name, String email){
    	Visitor visitor = new Visitor();
    	visitor.setId(id);
    	visitor.setName(name);
    	visitor.setEmail(email);
    	return visitor;
    }
    
    @Test
    public void shouldHaveAVisitor(){
    	Visitor visitor = createAVisitor(1L,"a name","an email");
    	comment.setVisitor(visitor);
    	assertEquals(visitor, comment.getVisitor());
    }
    
    private Theme createAThemeWithoutComments(Long id, String name, String url){
    	Theme theme = new Theme();
    	theme.setCreationDate(new Date());
    	theme.setId(id);
    	theme.setName(name);
    	theme.setUrl(url);
    	return theme;
    }

    @Test
    public void shouldHaveATheme(){
    	Theme theme;
    	theme = createAThemeWithoutComments(1L,"a name","an url");
    	comment.setTheme(theme);
    	assertEquals(theme, comment.getTheme());
    }    
    
    @Test
    public void shouldNotHaveANullLastUpdate(){
    	assertNotNull(comment.getLastUpdate());
    }
 
    @Test
    public void shouldUpdateWithTheCurrentTimeAndDate(){
    	Date almostPastDate = new Date();
    	comment.updateWithTheCurrentTimeAndDate();
    	assertTrue(almostPastDate.getTime()<=comment.getLastUpdate().getTime());
    }
}
