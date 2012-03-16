package br.com.bluesoft.desafio.controllers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;


import org.hibernate.Criteria;
import org.junit.Before;
import org.junit.Test;

import br.com.bluesoft.desafio.components.daos.CommentDao;
import br.com.bluesoft.desafio.components.daos.ThemeDao;
import br.com.bluesoft.desafio.components.daos.VisitorDao;
import br.com.bluesoft.desafio.entities.Comment;
import br.com.bluesoft.desafio.entities.Theme;
import br.com.bluesoft.desafio.entities.Visitor;
import br.com.bluesoft.desafio.examples.CommentsExample;
import br.com.bluesoft.desafio.examples.ThemesExample;
import br.com.bluesoft.desafio.examples.VisitorsExample;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.util.test.MockResult;
import br.com.caelum.vraptor.validator.Message;

public class CommentControllerTest {
    private Validator validator;
    private MockResult result;
    private CommentDao commentDao;
    private VisitorDao visitorDao;
    private ThemeDao themeDao;
    private CommentController controller;
	private Theme theme;
	private Comment comment;
	private Visitor visitor;
	private List<Comment> comments;    
    
    @Before
    public void setUp() {
    	validator = mock(Validator.class);
        result = new MockResult();
        commentDao = mock(CommentDao.class);
        themeDao = mock(ThemeDao.class);
        visitorDao = mock(VisitorDao.class);
        controller = new CommentController(commentDao,themeDao, visitorDao,validator,result);
    	comment = CommentsExample.createComment("a valid comment", 1L, new Date());
    	theme = ThemesExample.createTheme("a name", 1L, new Date(), "an url");
    	visitor = VisitorsExample.createVisitor("a name", 1L, "email@dominio");
    	comments = CommentsExample.createListWithTwoComments();
    }    
    
    private void assertTrueForMainIncludes(List<Comment> comments, Theme theme){
        assertTrue(result.included().containsKey("comments"));
        assertTrue(result.included().containsKey("themeId"));
        assertTrue(result.included().containsKey("themeName"));    	
        assertTrue(result.included().containsValue(comments));
        assertTrue(result.included().containsValue(theme.getId()));
        assertTrue(result.included().containsValue(theme.getName()));
    }
    
    private void assertTrueForSearchIncludes(String firstDate, String lastDate, String exactTextIntoVisitorEmail, String exactTextIntoComment){
        assertTrue(result.included().containsKey("numberOfPages"));
        assertTrue(result.included().containsKey("currentPage"));
        assertTrue(result.included().containsKey("firstDate"));   
        assertTrue(result.included().containsKey("lastDate"));
        assertTrue(result.included().containsKey("exactTextIntoVisitorEmail"));
        assertTrue(result.included().containsKey("exactTextIntoComment"));
        assertTrue(result.included().containsValue(firstDate));
        assertTrue(result.included().containsValue(lastDate));
        assertTrue(result.included().containsValue(exactTextIntoVisitorEmail));
        assertTrue(result.included().containsValue(exactTextIntoComment));
    }    
    
    private void assertTrueForStandbyCommentIncludes(Comment standByComment){
        assertTrue(result.included().containsKey("standByComment"));
        assertTrue(result.included().containsKey("standByTime"));
        assertTrue(result.included().containsKey("commentToEdition"));   
        assertTrue(result.included().containsValue((Integer)controller.getExpirationTimeToEditCommetInSeconds()/60));
        assertTrue(result.included().containsValue(standByComment));
        assertTrue(result.included().containsValue(null));
    } 
    
    private void assertTrueForEditionCommentIncludes(Comment commentToEdition){
        assertTrue(result.included().containsKey("standByComment"));
        assertTrue(result.included().containsKey("commentToEdition"));   
        assertTrue(result.included().containsValue(null));
        assertTrue(result.included().containsValue(commentToEdition));
    } 
    
    private void assertFalseForStandbyCommentIncludes(Comment standByComment){
        assertFalse(result.included().containsKey("standByComment"));
        assertFalse(result.included().containsKey("standByTime"));
        assertFalse(result.included().containsKey("commentToEdition"));   
        assertFalse(result.included().containsValue((Integer)controller.getExpirationTimeToEditCommetInSeconds()/60));
        assertFalse(result.included().containsValue(standByComment));
    } 
    
    private void defineOperationsToLoadCommentsByTheme(String url, Theme currentTheme, List<Comment> comments){
    	Criteria c1;
    	c1 = mock(Criteria.class);
    	
    	when(commentDao.createCriteriaToLoadCommentsByTheme(currentTheme)).thenReturn(c1);
    	when(commentDao.countComments(c1)).thenReturn(2L);
    	when(commentDao.loadComments(eq(c1),anyInt(),anyInt())).thenReturn(comments);
    	when(themeDao.loadTheme(currentTheme)).thenReturn(currentTheme);
    }

    @Test
    public void shouldLoadThemeByURLAndLoadItsComments(){
    	String url = "an url";
    	
    	when(themeDao.loadThemeByURL(url)).thenReturn(theme);
    	defineOperationsToLoadCommentsByTheme(url,theme,comments);
    	
    	controller.index(url,false);
    	
        verify(validator, never()).add((Message) any());
        assertTrueForMainIncludes(comments,theme);
    }
    
    @Test
    public void shouldLoadThemeByUnregisteredURLAndLoadItsComments(){
    	String url = "an unregistered url";
    	
    	when(themeDao.loadThemeByURL(url)).thenReturn(null);
    	controller.index(url,false);
        verify(validator, never()).add((Message) any());
        assertFalse(result.included().containsKey("comments"));
        assertFalse(result.included().containsKey("themeId"));
        assertFalse(result.included().containsKey("themeName"));
    }    
    
    private void defineOperationsToLoadCommentsByFilters(Theme currentTheme, Comment standByComment, List<Comment> comments,
    													Visitor visitor, String exactTextIntoVisitorEmail,String exactTextIntoComment){
    	Criteria c1;
    	c1 = mock(Criteria.class);
    	
    	when(commentDao.loadComment(standByComment)).thenReturn(standByComment);
    	
    	when(themeDao.loadTheme(currentTheme)).thenReturn(currentTheme);
    	when(commentDao.createCriteriaByFilters(eq(currentTheme), eq(visitor), (Date) any(), (Date) any(), 
    											eq(exactTextIntoVisitorEmail), eq(exactTextIntoComment))).thenReturn(c1);
    	when(commentDao.countComments(c1)).thenReturn(1L);
    	when(commentDao.loadComments(eq(c1),anyInt(),anyInt())).thenReturn(comments);
    }    
    
    private void defineOperationsToSetInitialPagination(Theme currentTheme, List<Comment> comments){
    	Criteria c1;
    	c1 = mock(Criteria.class);
    	
    	when(themeDao.loadTheme(currentTheme)).thenReturn(currentTheme);
    	when(commentDao.createCriteriaToLoadCommentsByTheme(currentTheme)).thenReturn(c1);
    	when(commentDao.countComments(c1)).thenReturn(1L);
    	when(commentDao.loadComments(eq(c1),anyInt(),anyInt())).thenReturn(comments);
    }
    
    @Test
    public void shouldFilterListOfCommentsByExactTextIntoVisitorEmail(){
    	Comment standByComment = null;
    	Visitor visitor = null;
    	String firstDate = "", lastDate = "";
    	String exactTextIntoVisitorEmail = "exact text into visitor email", exactTextIntoComment = "";
    	int currentPage = 1;

    	defineOperationsToLoadCommentsByFilters(theme, standByComment, comments,visitor, exactTextIntoVisitorEmail,exactTextIntoComment);
    	
		controller.filterCommentsList(theme, standByComment, firstDate, lastDate, exactTextIntoVisitorEmail, exactTextIntoComment, currentPage);
    	
        verify(validator, never()).add((Message) any());
        assertTrueForMainIncludes(comments,theme);
        assertTrueForSearchIncludes(firstDate,lastDate,exactTextIntoVisitorEmail,exactTextIntoComment);
    }    
 
    
    @Test
    public void shouldFilterListOfCommentsByExactTextIntoComment(){
    	Comment standByComment = null;
    	Visitor visitor = null;
    	String firstDate = "", lastDate = "";
    	String exactTextIntoVisitorEmail = "", exactTextIntoComment = "exact text into comment";
    	int currentPage = 1;

    	defineOperationsToLoadCommentsByFilters(theme, standByComment, comments,visitor, exactTextIntoVisitorEmail,exactTextIntoComment);
    	
		controller.filterCommentsList(theme, standByComment, firstDate, lastDate, exactTextIntoVisitorEmail, exactTextIntoComment, currentPage);

        verify(validator, never()).add((Message) any());
        assertTrueForMainIncludes(comments,theme);
        assertTrueForSearchIncludes(firstDate,lastDate,exactTextIntoVisitorEmail,exactTextIntoComment);
    }    
    
    @Test
    public void shouldFilterListOfCommentsByPeriodOfDates(){
    	Comment standByComment = null;
    	Visitor visitor = null;
    	String firstDate = "01/01/2011", lastDate = "02/01/2011";
    	String exactTextIntoVisitorEmail = "", exactTextIntoComment = "";
    	int currentPage = 1;
     	
    	defineOperationsToLoadCommentsByFilters(theme, standByComment, comments,visitor, exactTextIntoVisitorEmail,exactTextIntoComment);

    	controller.filterCommentsList(theme, standByComment, firstDate, lastDate, exactTextIntoVisitorEmail, exactTextIntoComment, currentPage);

        verify(validator, never()).add((Message) any());
        assertTrueForMainIncludes(comments,theme);
        assertTrueForSearchIncludes(firstDate,lastDate,exactTextIntoVisitorEmail,exactTextIntoComment);
    }    
    
    @Test
    public void shouldNotFilterListOfCommentsByOnlyFirstDate(){
    	Comment standByComment = null;
    	Visitor visitor = null;
    	String firstDate = "01/01/2011", lastDate = "";
    	String exactTextIntoVisitorEmail = "", exactTextIntoComment = "exact text into comment";
    	int currentPage = 1;

        when(validator.onErrorRedirectTo(CommentController.class)).thenReturn(controller);
    	defineOperationsToLoadCommentsByFilters(theme, standByComment, comments,visitor, exactTextIntoVisitorEmail,exactTextIntoComment);
    	
		controller.filterCommentsList(theme, standByComment, firstDate, lastDate, exactTextIntoVisitorEmail, exactTextIntoComment, currentPage);

        verify(validator).add((Message) any());   
        assertTrue(result.included().containsKey("showInputErrorsByHTML")); 
        assertTrueForSearchIncludes(firstDate,lastDate,exactTextIntoVisitorEmail,exactTextIntoComment);
    }    
    
    @Test
    public void shouldNotFilterListOfCommentsByOnlyLastDate(){
    	Comment standByComment = null;
    	Visitor visitor = null;
    	String firstDate = "", lastDate = "01/01/2011";
    	String exactTextIntoVisitorEmail = "", exactTextIntoComment = "exact text into comment";
    	int currentPage = 1;

        when(validator.onErrorRedirectTo(CommentController.class)).thenReturn(controller);
    	defineOperationsToLoadCommentsByFilters(theme, standByComment, comments,visitor, exactTextIntoVisitorEmail,exactTextIntoComment);
    	
		controller.filterCommentsList(theme, standByComment, firstDate, lastDate, exactTextIntoVisitorEmail, exactTextIntoComment, currentPage);

        verify(validator).add((Message) any());   
        assertTrue(result.included().containsKey("showInputErrorsByHTML")); 
        assertTrueForSearchIncludes(firstDate,lastDate,exactTextIntoVisitorEmail,exactTextIntoComment);
    }   
    
    @Test
    public void shouldAddValidCommentWithExistingVisitor(){
    	comment.setVisitor(visitor);
    	comment.setTheme(theme);
    	
    	when(themeDao.loadTheme(comment.getTheme())).thenReturn(theme);
    	when(visitorDao.loadVisitorByEmail(comment.getVisitor().getEmail())).thenReturn(visitor);
    	when(commentDao.addComment(comment)).thenReturn(comment.getId());
    	defineOperationsToSetInitialPagination(theme,comments);
    	
    	controller.addNewComment(comment);
    	
        verify(validator, never()).add((Message) any());
        assertTrueForMainIncludes(comments,theme);
        assertTrueForStandbyCommentIncludes(comment);
    	
    }
    
    @Test
    public void shouldAddValidCommentWithANewVisitor(){
    	comment.setVisitor(visitor);
    	comment.setTheme(theme);
    	
    	when(themeDao.loadTheme(comment.getTheme())).thenReturn(theme);
    	when(visitorDao.loadVisitorByEmail(comment.getVisitor().getEmail())).thenReturn(null);
    	when(visitorDao.addVisitor(visitor)).thenReturn(visitor.getId());
    	when(visitorDao.loadVisitor(visitor)).thenReturn(visitor);
    	when(commentDao.addComment(comment)).thenReturn(comment.getId());

    	defineOperationsToSetInitialPagination(theme,comments);
    	
    	controller.addNewComment(comment);
    	
        verify(validator, never()).add((Message) any());
        assertTrueForMainIncludes(comments,theme);
        assertTrueForStandbyCommentIncludes(comment);
    	
    }    
    
    @Test
    public void shouldNotAddCommentWithEmptyDescription(){
    	comment.setDescription("");
    	comment.setVisitor(visitor);
    	comment.setTheme(theme);

    	when(themeDao.loadTheme(theme)).thenReturn(theme);
    	when(validator.onErrorRedirectTo(CommentController.class)).thenReturn(controller);
    	defineOperationsToSetInitialPagination(theme,comments);
    	
    	controller.addNewComment(comment);
    	
        verify(validator).add((Message) any());   
        assertTrue(result.included().containsKey("showInputErrorsByHTML")); 
    }        

    @Test
    public void shouldNotAddCommentWithEmptyEmail(){
    	visitor.setEmail("");
    	comment.setVisitor(visitor);
    	comment.setTheme(theme);

    	when(themeDao.loadTheme(theme)).thenReturn(theme);
    	when(validator.onErrorRedirectTo(CommentController.class)).thenReturn(controller);
    	defineOperationsToSetInitialPagination(theme,comments);
    	
    	controller.addNewComment(comment);
    	
        verify(validator,times(2)).add((Message) any());   
        assertTrue(result.included().containsKey("showInputErrorsByHTML")); 
    }     
    
    @Test
    public void shouldNotAddCommentWithInvalidEmail(){
    	visitor.setEmail("semarroba.com");
    	comment.setVisitor(visitor);
    	comment.setTheme(theme);

    	when(themeDao.loadTheme(theme)).thenReturn(theme);
    	when(validator.onErrorRedirectTo(CommentController.class)).thenReturn(controller);
    	defineOperationsToSetInitialPagination(theme,comments);
    	
    	controller.addNewComment(comment);
    	
        verify(validator).add((Message) any());   
        assertTrue(result.included().containsKey("showInputErrorsByHTML")); 
    }       
    
    @Test
    public void shouldNotAddCommentWithEmptyEmailAndDescription(){
    	visitor.setEmail("");
    	comment.setDescription("");
    	comment.setVisitor(visitor);
    	comment.setTheme(theme);

    	when(themeDao.loadTheme(theme)).thenReturn(theme);
    	when(validator.onErrorRedirectTo(CommentController.class)).thenReturn(controller);
    	defineOperationsToSetInitialPagination(theme,comments);
    	
    	controller.addNewComment(comment);
    	
        verify(validator,times(3)).add((Message) any());   
        assertTrue(result.included().containsKey("showInputErrorsByHTML")); 
    }       

    @Test
    public void shouldUpdateValidCommentWithExistingVisitor(){
    	comment.setVisitor(visitor);
    	comment.setTheme(theme);
    	
    	when(themeDao.loadTheme(comment.getTheme())).thenReturn(theme);
    	when(visitorDao.loadVisitorByEmail(comment.getVisitor().getEmail())).thenReturn(visitor);
    	when(commentDao.updateComment(comment)).thenReturn(comment);
    	defineOperationsToSetInitialPagination(theme,comments);
    	
    	controller.updateComment(comment);
    	
        verify(validator, never()).add((Message) any());
        assertTrueForMainIncludes(comments,theme);
        assertTrueForStandbyCommentIncludes(comment);
    }
    
    @Test
    public void shouldUpdateValidCommentWithANewVisitor(){
    	comment.setVisitor(visitor);
    	comment.setTheme(theme);
    	
    	when(themeDao.loadTheme(comment.getTheme())).thenReturn(theme);
    	when(visitorDao.loadVisitorByEmail(comment.getVisitor().getEmail())).thenReturn(null);
    	when(visitorDao.addVisitor(visitor)).thenReturn(visitor.getId());
    	when(visitorDao.loadVisitor(visitor)).thenReturn(visitor);
    	when(commentDao.updateComment(comment)).thenReturn(comment);

    	defineOperationsToSetInitialPagination(theme,comments);
    	
    	controller.updateComment(comment);
    	
        verify(validator, never()).add((Message) any());
        assertTrueForMainIncludes(comments,theme);
        assertTrueForStandbyCommentIncludes(comment);
    	
    }    
    
    @Test
    public void shouldNotUpdateCommentWithEmptyDescription(){
    	comment.setDescription("");
    	comment.setVisitor(visitor);
    	comment.setTheme(theme);

    	when(themeDao.loadTheme(theme)).thenReturn(theme);
    	when(validator.onErrorRedirectTo(CommentController.class)).thenReturn(controller);
    	defineOperationsToSetInitialPagination(theme,comments);
    	
    	controller.updateComment(comment);
    	
        verify(validator).add((Message) any());   
        assertTrue(result.included().containsKey("showInputErrorsByHTML")); 
    }     
    
    @Test
    public void shouldNotUpdateCommentWithEmptyEmail(){
    	visitor.setEmail("");
    	comment.setVisitor(visitor);
    	comment.setTheme(theme);

    	when(themeDao.loadTheme(theme)).thenReturn(theme);
    	when(validator.onErrorRedirectTo(CommentController.class)).thenReturn(controller);
    	defineOperationsToSetInitialPagination(theme,comments);
    	
    	controller.updateComment(comment);
    	
        verify(validator,times(2)).add((Message) any());   
        assertTrue(result.included().containsKey("showInputErrorsByHTML")); 
    }     
    
    @Test
    public void shouldNotUpdateCommentWithInvalidEmail(){
    	visitor.setEmail("semarroba.com");
    	comment.setVisitor(visitor);
    	comment.setTheme(theme);

    	when(themeDao.loadTheme(theme)).thenReturn(theme);
    	when(validator.onErrorRedirectTo(CommentController.class)).thenReturn(controller);
    	defineOperationsToSetInitialPagination(theme,comments);
    	
    	controller.updateComment(comment);
    	
        verify(validator).add((Message) any());   
        assertTrue(result.included().containsKey("showInputErrorsByHTML")); 
    }  
    
    @Test
    public void shouldNotUpdateCommenttWithEmptyEmailAndDescription(){
    	visitor.setEmail("");
    	comment.setDescription("");
    	comment.setVisitor(visitor);
    	comment.setTheme(theme);

    	when(themeDao.loadTheme(theme)).thenReturn(theme);
    	when(validator.onErrorRedirectTo(CommentController.class)).thenReturn(controller);
    	defineOperationsToSetInitialPagination(theme,comments);
    	
    	controller.updateComment(comment);
    	
        verify(validator,times(3)).add((Message) any());   
        assertTrue(result.included().containsKey("showInputErrorsByHTML")); 
    }   
    
    @Test
    public void shouldPrepareCommentToEdition(){
    	comment.setVisitor(visitor);
    	comment.setTheme(theme);
    	
    	when(commentDao.loadComment(comment)).thenReturn(comment);
    	when(themeDao.loadTheme(comment.getTheme())).thenReturn(theme);
    	defineOperationsToSetInitialPagination(theme,comments);
    	
    	controller.prepareCommentToEdition(comment);
    	
        verify(validator, never()).add((Message) any());
        assertTrueForMainIncludes(comments,theme);
        assertTrueForEditionCommentIncludes(comment);    	
    }   

    @Test
    public void shouldDeleteComment(){
    	comment.setVisitor(visitor);
    	comment.setTheme(theme);
    	
    	when(commentDao.loadComment(comment)).thenReturn(comment);
    	when(themeDao.loadTheme(comment.getTheme())).thenReturn(theme);
    	defineOperationsToSetInitialPagination(theme,comments);
    	
    	controller.deleteComment(comment);
    	
        verify(validator, never()).add((Message) any());
        assertTrueForMainIncludes(comments,theme);
        assertFalseForStandbyCommentIncludes(comment);
    }      
    
    @Test
    public void shouldRedirectToNewTheme(){
    	controller.redirectToNewThemeForm();
    	verify(validator, never()).add((Message) any());
    }
    
    @Test
    public void shouldHaveAnExpirationTimeToEditCommetInSeconds(){
    	int timeInseconds = 60;
    	controller.setExpirationTimeToEditCommetInSeconds(timeInseconds);
    	assertTrue(timeInseconds == controller.getExpirationTimeToEditCommetInSeconds());
    }
    
    @Test
    public void shouldHaveResultsByPage(){
    	int resultsByPage = 10;
    	controller.setResultsByPage(resultsByPage);
    	assertTrue(resultsByPage == controller.getResultsByPage());
    }    
    
    
}




