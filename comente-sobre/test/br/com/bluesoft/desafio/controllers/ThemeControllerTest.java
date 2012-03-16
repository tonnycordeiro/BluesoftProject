package br.com.bluesoft.desafio.controllers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.bluesoft.desafio.components.daos.ThemeDao;
import br.com.bluesoft.desafio.components.utils.TextualSearch_PT_BR;
import br.com.bluesoft.desafio.entities.Theme;
import br.com.bluesoft.desafio.examples.ThemesExample;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.util.test.MockResult;
import br.com.caelum.vraptor.validator.Message;

public class ThemeControllerTest {
    private Validator validator;
    private MockResult result;
    private ThemeDao themeDao;
    private ThemeController controller;
	private Theme theme;
	private List<Theme> themes;

    @Before
    public void setUp() {
    	validator = mock(Validator.class);
        result = new MockResult();
        themeDao = mock(ThemeDao.class);
        controller = new ThemeController(themeDao, validator,result);
    	theme = ThemesExample.createTheme("a name", 1L, new Date(), "an url");
    	themes = ThemesExample.createListWithTwoThemes();
    }    

    @Test
    public void shouldDefineTheChoosenThemeAndRedirectToPostComments(){
    	/*TODO: improve the test*/
    	when(themeDao.loadTheme(theme)).thenReturn(theme);
    	controller.defineChoosenTheme(theme);
    	verify(validator, never()).add((Message) any());
    }
    
    @Test
    public void shouldAddNewTheme(){
    	/*TODO: improve the test*/
    	List<Theme> themesList = new ArrayList<Theme>();    	
    	when(themeDao.loadThemeByName(theme.getName())).thenReturn(null);
    	when(themeDao.searchThemeWithKeyWords(new TextualSearch_PT_BR()
    										.chooseKeyWordsInTheSearchString(theme.getName()))).thenReturn(themesList);
    	when(themeDao.loadThemeByURL(theme.getUrl())).thenReturn(null);
    	when(themeDao.addTheme(theme)).thenReturn(theme.getId());
    	controller.tryAddNewTheme(theme);
    	verify(validator, never()).add((Message) any());
    }
    
    @Test
    public void shouldVerifyAnIncompatibleThemeByUrl(){

    	theme.createAndSetUrlByThemeName();
    	
    	when(themeDao.loadThemeByURL(theme.getUrl())).thenReturn(theme);
    	when(validator.onErrorRedirectTo(ThemeController.class)).thenReturn(controller);
    	controller.prepareAndAddNewTheme(theme);
    	verify(validator).add((Message) any());
    	assertTrue(result.included().containsKey("newTheme"));
    	assertTrue(result.included().containsValue(theme));
    }
    
    @Test
    public void shouldShowListOfThemes(){
    	
    	when(themeDao.loadThemeByName(theme.getName())).thenReturn(theme);
    	
    	controller.showListOfThemes(themes);
    	assertTrue(result.included().containsKey("suggestionsThemes"));
    	assertTrue(result.included().containsValue(themes));    	
    }
    
    @Test
    public void shouldLoadOldThemeAndRedirectToPostComments(){
    	/*TODO: improve the test*/
    	when(themeDao.loadThemeByName(theme.getName())).thenReturn(theme);
    	controller.tryAddNewTheme(theme);
    	verify(validator, never()).add((Message) any());    	
    }
    
    @Test
    public void shouldGenerateSuggestionsForACandidateTheme(){
    	when(themeDao.loadThemeByName(theme.getName())).thenReturn(null);
    	when(themeDao.searchThemeWithKeyWords(
    			new TextualSearch_PT_BR().chooseKeyWordsInTheSearchString(theme.getName()))).thenReturn(themes);
    	controller.tryAddNewTheme(theme);
    	assertTrue(result.included().containsKey("newTheme"));
    	assertTrue(result.included().containsValue(theme));
    	assertTrue(result.included().containsKey("suggestionsThemes"));
    	assertTrue(result.included().containsValue(themes));
    }
    
    @Test
    public void shouldNotGenerateSuggestionsForACandidateTheme(){
    	when(themeDao.loadThemeByName(theme.getName())).thenReturn(null);
    	when(themeDao.searchThemeWithKeyWords(
    			new TextualSearch_PT_BR().chooseKeyWordsInTheSearchString(theme.getName()))).thenReturn(new ArrayList<Theme>());
    	controller.tryAddNewTheme(theme);
    	assertFalse(result.included().containsKey("newTheme"));
    	assertFalse(result.included().containsValue(theme));
    	assertFalse(result.included().containsKey("suggestionsThemes"));
    	assertFalse(result.included().containsValue(themes));
    }    
    
    @Test
    public void shouldNotAddAnEmptyThemeWithInvalidComment(){
    	theme.setName("");
    	when(validator.onErrorRedirectTo(ThemeController.class)).thenReturn(controller);
    	controller.tryAddNewTheme(theme);
    	verify(validator).add((Message) any());
    }    

    
}

