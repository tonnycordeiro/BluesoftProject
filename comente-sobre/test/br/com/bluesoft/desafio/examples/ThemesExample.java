package br.com.bluesoft.desafio.examples;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.bluesoft.desafio.entities.Theme;

public class ThemesExample {
    public static Theme createTheme(String name,Long id,Date creationDate, String url) {
    	Theme theme = new Theme();
    	theme.setId(id);
        theme.setName(name);
        theme.setCreationDate(creationDate);
        theme.setUrl(url);
        
        return theme;
    }
	
	public static List<Theme> createListWithTwoThemes(){
    	List<Theme> themesList = new ArrayList<Theme>();
    	themesList.add(createTheme("a name 1",1L,new Date(),"a url 1"));
    	themesList.add(createTheme("a name 2",2L,new Date(),"a url 2"));    	
    	return themesList;
    }    

}
