package br.com.bluesoft.desafio.components.daos;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.bluesoft.desafio.components.utils.HibernateOrder;
import br.com.bluesoft.desafio.entities.Theme;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;


@Component
@ApplicationScoped
public class ThemeDao {

    private final Session session;
    private HibernateOrder order;

    public ThemeDao(Session session) {
        this.session = session;
    }

	public HibernateOrder getOrder() {
		return order;
	}

	public void setOrder(HibernateOrder order) {
		this.order = order;
	}

	public void defineOrder(String field,boolean isAsc){
		this.order = new HibernateOrder(field, isAsc);
	}
	
    public boolean themeExists(Theme theme) {
        return loadThemeByName(theme.getName()) != null;
    }
    
    public Long addTheme(Theme theme) {
    	Long idNewTheme = null;
        if (!themeExists(theme)) {
            Transaction tx = this.session.beginTransaction();
            idNewTheme = (Long)this.session.save(theme);
            tx.commit();
        }
        return idNewTheme;
    }
    
    public Theme updateTheme(Theme theme) {
        if (themeExists(theme)) {
            Transaction tx = this.session.beginTransaction();
            theme = (Theme)this.session.merge(theme);
            tx.commit();
            return theme;
        }
        return null;
    }

    public Theme loadTheme(Theme theme) {
        if (theme == null)
        	return null;
        return (Theme) session.createCriteria(Theme.class)
                .add(Restrictions.eq("id",theme.getId())).uniqueResult();
    }

    public Theme loadThemeByName(String name) {
        if (name == null)
        	return null;
        return (Theme) session.createCriteria(Theme.class)
                .add(Restrictions.eq("name",name)).uniqueResult();
    }    

    public Theme loadThemeByURL(String url){
    	if (url == null)
    		return null;
        return (Theme)this.session.createCriteria(Theme.class)
                .add(Restrictions.eq("url", url)).uniqueResult();
    }
    
    public void deleteTheme(Theme theme) {
        Theme deletedTheme = loadTheme(theme);
        if (deletedTheme != null) {
            Transaction tx = this.session.beginTransaction();
            this.session.delete(deletedTheme);
            tx.commit();
        }
    }

    private Criteria createCriteriaWithDefaultOrder(Session session){
    	Criteria criteria = this.session.createCriteria(Theme.class);
    	if(this.order != null)
    		criteria = this.order.addDefaultOrder(criteria);
    	return criteria;
    }
    
    @SuppressWarnings("unchecked")
	public List<Theme> searchThemeWithKeyWords(List<String> keys){
    	Iterator<String> iterator = keys.iterator();
    	Disjunction disjunction = Restrictions.disjunction();
    	Criteria criteria = createCriteriaWithDefaultOrder(this.session);
    	
    	while(iterator.hasNext()) {
    		disjunction.add(Restrictions.ilike("name",iterator.next(),MatchMode.ANYWHERE));
    	} 
    	
    	return (List<Theme>)criteria.add(disjunction).list();
    }
    
}
