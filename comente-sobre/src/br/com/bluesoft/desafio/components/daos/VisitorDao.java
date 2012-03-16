package br.com.bluesoft.desafio.components.daos;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.com.bluesoft.desafio.entities.Visitor;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class VisitorDao {

    private final Session session;

    public VisitorDao(Session session) {
        this.session = session;
    }

    public boolean visitorExists(Visitor visitor) {
        return loadVisitor(visitor) != null;
    }
    
    public Long addVisitor(Visitor visitor) {
    	Long idNewVisitor = null;
        if (!visitorExists(visitor)) {
            Transaction tx = this.session.beginTransaction();
            idNewVisitor = (Long)this.session.save(visitor);
            tx.commit();
        }
        return idNewVisitor;
    }
    
    public Visitor updateVisitor(Visitor visitor) {
        if (visitorExists(visitor)) {
            Transaction tx = this.session.beginTransaction();
            visitor = (Visitor)this.session.merge(visitor);
            tx.commit();
            return visitor;
        }
        return null;
    }

    public Visitor loadVisitor(Visitor visitor) {
        if (visitor == null) 
            return null;
        return (Visitor) session.createCriteria(Visitor.class)
                .add(Restrictions.eq("id",visitor.getId())).uniqueResult();
    }

    public Visitor loadVisitorByEmail(String email) {
        if (email == null) 
            return null;
        return (Visitor) session.createCriteria(Visitor.class)
                .add(Restrictions.eq("email",email)).uniqueResult();
    }
    
    public void deleteVisitor(Visitor visitor) {
        Visitor deletedVisitor = loadVisitor(visitor);
        if (deletedVisitor != null) {
            Transaction tx = this.session.beginTransaction();
            this.session.delete(deletedVisitor);
            tx.commit();
        }
    }
    
}
