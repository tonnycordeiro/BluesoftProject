package br.com.bluesoft.desafio.controllers;

import java.util.ArrayList;
import java.util.List;

import br.com.bluesoft.desafio.components.daos.ThemeDao;
import br.com.bluesoft.desafio.components.utils.DataFromViewManager;
import br.com.bluesoft.desafio.components.utils.DataToViewManager;
import br.com.bluesoft.desafio.components.utils.TextualSearch_PT_BR;
import br.com.bluesoft.desafio.entities.Theme;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class ThemeController {

    private final ThemeDao themeDao;
    private final Validator validator;
    private final Result result;

    public ThemeController(ThemeDao themeDao, Validator validator, Result result) {
        this.themeDao = themeDao;
        this.validator = validator;
        this.result = result;
        themeDao.defineOrder("name", true);
    }
    
    @Path("/")
    public void index(){
    }
    
    @Path("/theme/list")
    public void showListOfThemes(List<Theme> themesList){
    	result.include("suggestionsThemes",themesList);
    	result.redirectTo(ThemeController.class).index();
    }
    
    @Post("/theme/add")
    public void prepareAndAddNewTheme(Theme newTheme){
    	unescapeFields(newTheme);
    	addNewTheme(newTheme);
    }
    
    @Post("/theme/try_add")
    public void tryAddNewTheme(Theme candidateTheme){
    	Theme themeIntoDB;
    	
    	unescapeFields(candidateTheme);
    	validateContent(candidateTheme);    	

    	candidateTheme.nameToUpper();
    	themeIntoDB = themeDao.loadThemeByName(candidateTheme.getName());
    	
    	if(themeIntoDB == null)
    		generateThemesSuggestionsByKeyWords(candidateTheme);
    	else
    		prepareToAddComments(themeIntoDB);
    }
	
    @Path("/theme/choosen")
    public void defineChoosenTheme(Theme theme){
    	theme = themeDao.loadTheme(theme);
    	prepareToAddComments(theme);
    }    

    private void addNewTheme(Theme newTheme){
    	Theme existingTheme;
    	String url, error;
    	List<String> errors = new ArrayList<String>();
    	
    	newTheme.setUrl(url = newTheme.createAndSetUrlByThemeName());
    	existingTheme = themeDao.loadThemeByURL(url);
    	
    	if(existingTheme != null){
    		errors.add(error = "Mudar o nome do tema (verificar acentuação), pois já existe um tema diferente que aponta para a mesma página de postagem de comentários");
    		showErrors(errors,"Favor consertar a(s) pendência(s) abaixo:");
    		result.include("newTheme",newTheme);
    		validator.add(new ValidationMessage(error,"Dados incompatíveis"));
    		validator.onErrorRedirectTo(ThemeController.class).index();
    	}
    	else{
	    	newTheme.setId(themeDao.addTheme(newTheme));
	    	prepareToAddComments(newTheme);
    	}
    }

    private void prepareToAddComments(Theme theme){
    	result.redirectTo(CommentController.class).index(theme.getUrl(),false);
    }

    private void generateThemesSuggestionsByKeyWords(Theme theme){
		List<Theme> themesList;
		themesList = themeDao.searchThemeWithKeyWords(new TextualSearch_PT_BR().chooseKeyWordsInTheSearchString(theme.getName()));
		if(themesList.isEmpty()){
			addNewTheme(theme);
		}
		else{
	    	result.include("newTheme",theme);
	        showListOfThemes(themesList);
		}
	}

	private void validateContent(Theme theme){
    	Boolean isOK = true;
    	String error;
    	List<String> errors = new ArrayList<String>();
    	
    	if (theme.getName().isEmpty() || theme.getName() == null){
    		isOK = false;
    		errors.add(error = "O assunto deve conter ao menos uma palavra chave");
            validator.add(new ValidationMessage(error,"Dados inválidos"));
    	}
    	
    	if(!isOK){
    		showErrors(errors,"Favor consertar a(s) pendência(s) abaixo:");
    	}
    }

    private void unescapeFields(Theme theme){
		theme.setName(DataFromViewManager.unescapeStringReturnedFromJavascriptMethodEscape(theme.getName()));
	}

	private void showErrors(List<String> errors,String variableToInclude){
    	String errorBlockHTML;
        errorBlockHTML = DataToViewManager.buildHtmlMessage(errors, "Favor consertar a(s) pendência(s) abaixo:");
        result.include("showInputErrorsByHTML", errorBlockHTML);
		validator.onErrorRedirectTo(ThemeController.class).index();
    }    

}
