package br.com.bluesoft.desafio.components.factories;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
@ApplicationScoped
public class SessionFactoryCreator implements ComponentFactory<SessionFactory> {

    private final Configuration configuration;
    private SessionFactory factory;

    public SessionFactoryCreator(Configuration configuration) {
        this.configuration = configuration;
    }

    @PostConstruct
    public void buildSessionFactory() {
        factory = configuration.configure().buildSessionFactory();
    }

    @PreDestroy
    public void closeFactory() {
        this.factory.close();
    }

    public SessionFactory getInstance() {
        return factory;
    }

}
