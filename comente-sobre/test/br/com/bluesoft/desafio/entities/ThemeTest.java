package br.com.bluesoft.desafio.entities;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;

import br.com.bluesoft.desafio.examples.CommentsExample;

public class ThemeTest {
	private Theme theme;

	private Long id;
	private String name;
	private Date creationDate;
	private String url;

	@Before
    public void createThemeWithBasicValues() {
    	theme = new Theme();
    	
    	id = 1L;
    	name = "a theme name";
    	creationDate = new Date();
    	url = "an url";
    	
    	theme.setId(id);
    	theme.setCreationDate(creationDate);
    	theme.setName(name);
    	theme.setUrl(url);
    	theme.setComments(new ArrayList<Comment>());
    	
    }
    
    @Test
    public void shouldHaveAnId(){
    	assertEquals(id, theme.getId());
    }
    
    @Test
    public void shouldHaveAName(){
    	assertEquals(name, theme.getName());
    }
    
    @Test
    public void shouldHaveACreationDate(){
    	assertEquals(creationDate, theme.getCreationDate());
    }
    
    @Test
	public void shouldHaveAUrl() {
    	assertEquals(url, theme.getUrl());
    }

    @Test
    public void shouldConvertToStringWithMainAttributes(){
    	String strTheme = theme.toString();
    	assertTrue(strTheme.contains(id.toString()));
    	assertTrue(strTheme.contains(name));
    	assertTrue(strTheme.contains(creationDate.toString()));
    	assertTrue(strTheme.contains(url));
    }
    
    @Test
	public void shouldConvertANameToUpper() {
    	String miscellaniusName = "aBc";
    	String upperName = "ABC";
    	theme.setName(miscellaniusName);
    	theme.nameToUpper();
    	assertEquals(upperName, theme.getName());
    }
    
    @Test
	public void shouldCreateAnUrlByThemeNameWithoutReservedChars() {
    	String patternOfNotReservedSymbolsOfURL = "\\p{Alnum}-_.~";
    	String asciiSymbols=" 0123456789" +
    						"'-ñó!#$%&()*,./:;?@[]à^_`{|}~°¶®Ø¥∏øòëíÇìîÑãõ¢£§•Ä+<=>±´ª◊˜ß©¨Æ∞µ∂∑ÖÜáïâºΩæπ≤≥" +
    						"Aa™¡·¿‡¬‚ƒ‰√„≈Â∆ÊBbCc«ÁDd–Ee…È»Ë ÍÀÎFfÉGgHhIiÕÌÃÏŒÓœÔJjKkLlMmNn—ÒOo∫”Û“Ú‘Ù÷ˆ’ı" +
    						"ÿ¯åúPpQqRrSsäöﬂTtﬁ˛ôUu⁄˙Ÿ˘€˚‹¸VvWwXxYy›˝üˇZzéû" + '"' + "\\";
    	theme.setName(asciiSymbols);
    	theme.createAndSetUrlByThemeName();
    	assertFalse(theme.getUrl().matches(patternOfNotReservedSymbolsOfURL));
    }    
    
    @Test
	public void shouldCreateAnUrlWithOnlyLowerCase() {
    	theme.setName("ABCabc");
    	theme.createAndSetUrlByThemeName();
    	assertEquals(theme.getUrl(),"abcabc");
    }

    @Test
	public void shouldCreateAnUrlWithHifenInsteadSpaces() {
    	theme.setName(" 1  2   3    ");
    	theme.createAndSetUrlByThemeName();
    	assertEquals(theme.getUrl(),"-1--2---3----");
    }

    @Test
	public void shouldNotCreateAnUrlWithNullName() {
    	String url;
    	theme.setName(null);
    	url = theme.createAndSetUrlByThemeName();
    	assertNull(url);
    }

    private Comment[] addListWithTwoCommentsInTheTheme(){
    	Comment[] comments = CommentsExample.createArrayWithTwoComments(); 

    	theme.addComment(comments[0]);
    	theme.addComment(comments[1]);
    	
    	return comments;
    }

    private Comment[] addListWithOneCommentInTheTheme(){
    	Comment[] comments = new Comment[1];
    	comments[0] = CommentsExample.createComment("a description 1",1L,new Date());

    	theme.addComment(comments[0]);
    	
    	return comments;
    }
    
    @Test
    public void shouldHaveAListOfTwoComments(){
    	Comment[] comments;
    	comments = addListWithTwoCommentsInTheTheme();
    	
    	assertTrue(theme.getComments().contains(comments[0]));
    	assertTrue(theme.getComments().contains(comments[1]));
        assertEquals(2, theme.getComments().size());
    }

    @Test
    public void shouldNotHaveAListOfComments(){
        assertEquals(0, theme.getComments().size());
    }
    
    @Test
    public void shoudAddOneCommentIfThemeHaveZeroComments(){
    	Comment comments[];
    	comments = addListWithOneCommentInTheTheme();
    	
    	assertTrue(theme.getComments().contains(comments[0]));
    	assertEquals(1, theme.getComments().size());
    }    

    @Test
    public void shoudAddOneCommentIfThemeHaveOneComment(){
    	Comment comment;
    	
    	addListWithOneCommentInTheTheme();    	
    	comment = CommentsExample.createComment("a description 2",2L,new Date());
    	theme.addComment(comment);

    	assertTrue(theme.getComments().contains(comment));
    	assertEquals(2, theme.getComments().size());
    }    

    
    @Test
    public void shouldRemoveOneCommentIfThemeHaveTwoComments(){
    	Comment[] comments;
    	comments = addListWithTwoCommentsInTheTheme();
    	
    	theme.removeComment(comments[0]);
    	assertFalse(theme.getComments().contains(comments[0]));
    	assertTrue(theme.getComments().contains(comments[1]));
        assertEquals(1, theme.getComments().size());
    }
    
    @Test
    public void shouldRemoveOneCommentIfThemeHaveOneComment(){
    	Comment[] comments;
    	comments = addListWithOneCommentInTheTheme();
    	
    	theme.removeComment(comments[0]);
    	assertFalse(theme.getComments().contains(comments[0]));
        assertEquals(0, theme.getComments().size());
    }
    
    @Test
    public void shouldNotRemoveOneCommentIfThemeHaveZeroComments(){
    	Comment comment = CommentsExample.createComment("a description",1L,new Date());

    	theme.removeComment(comment);
    	assertFalse(theme.getComments().contains(comment));
        assertEquals(0, theme.getComments().size());
    }
    
}
