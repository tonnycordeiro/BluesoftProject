package br.com.bluesoft.desafio.components.daos;

import java.util.List;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.bluesoft.desafio.components.utils.HibernateOrder;
import br.com.bluesoft.desafio.entities.Comment;
import br.com.bluesoft.desafio.entities.Theme;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.bluesoft.desafio.entities.Visitor;

@Component
@ApplicationScoped
public class CommentDao {

    private final Session session;
    private HibernateOrder order;
    
    public CommentDao(Session session) {
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
	
    private Criteria addDefaultOrder(Criteria criteria){
		if(this.order != null)
			criteria = this.order.addDefaultOrder(criteria);
		return criteria;
	}

	public boolean commentExists(Comment comment) {
        return loadComment(comment) != null;
    }


    public Comment loadComment(Comment comment) {
        if (comment == null)
            return null;
        return (Comment) session.createCriteria(Comment.class)
                .add(Restrictions.eq("id",comment.getId())).uniqueResult();
    }    
    
    public Long addComment(Comment comment) {
    	Long idNewComment = null;
        if (!commentExists(comment)) {
            Transaction tx = this.session.beginTransaction();
            idNewComment = (Long)this.session.save(comment);
            tx.commit();
        }
        return idNewComment;
    }

    public Comment updateComment(Comment comment) {
        if (commentExists(comment)) {
            Transaction tx = this.session.beginTransaction();
            comment = (Comment)this.session.merge(comment);
            tx.commit();
            return comment;
        }
        return null;
    }

    public void deleteComment(Comment comment) {
        Comment deletedComment = loadComment(comment);
        if (deletedComment != null) {
            Transaction tx = this.session.beginTransaction();
            this.session.delete(deletedComment);
            tx.commit();
        }
    }

    public Criteria createCriteriaToLoadAllComments() {
		Criteria criteria = this.session.createCriteria(Comment.class);
		return criteria;
    }

    public Criteria createCriteriaToLoadCommentsByVisitor(Visitor visitor) {
		Criteria criteria = this.session.createCriteria(Comment.class);
		criteria.add(Restrictions.eq("visitor.id", visitor.getId()));
        return criteria;
    }

    public Criteria createCriteriaToLoadCommentsByTheme(Theme theme) {
		Criteria criteria = this.session.createCriteria(Comment.class);
        criteria.add(Restrictions.eq("theme.id", theme.getId()));
        return criteria;
    }    


    public Criteria createCriteriaByFilters(Theme theme,Visitor visitor,Date firstDate,
    		Date lastDate,String exactTextIntoVisitorEmail,String exactTextIntoComment) {
    	
    	Criteria filtersCriteria = this.session.createCriteria(Comment.class);
    	
    	if(theme != null)
    		filtersCriteria.add(Restrictions.eq("theme.id", theme.getId()));
    	if(visitor != null)
    		filtersCriteria.add(Restrictions.eq("visitor.id", visitor.getId()));
    	if(firstDate != null && lastDate !=null)
    		filtersCriteria.add(Restrictions.between("lastUpdate",
    				firstDate,lastDate));
    	if(exactTextIntoVisitorEmail != null && !exactTextIntoVisitorEmail.isEmpty()){
    		filtersCriteria.createAlias("visitor","visitor")
    					   .setFetchMode("visitor", FetchMode.JOIN)
    					   .add(Restrictions.ilike("visitor.email", exactTextIntoVisitorEmail, MatchMode.ANYWHERE));
    	}
    	if(exactTextIntoComment != null && !exactTextIntoComment.isEmpty())
    		filtersCriteria.add(Restrictions.ilike("description", exactTextIntoComment,MatchMode.ANYWHERE));    	

    	return filtersCriteria;
    }    
    
    @SuppressWarnings("unchecked")
	public List<Comment> loadComments(Criteria criteria,int resultsByPage, int firstResult){
    	addDefaultOrder(criteria);
    	return (List<Comment>) criteria.setFirstResult(firstResult)
				  .setMaxResults(resultsByPage).list();    	
    }
    
    public long countComments(Criteria criteria){
    	return (Long)criteria.setProjection(Projections.rowCount()).uniqueResult();
    }
}
