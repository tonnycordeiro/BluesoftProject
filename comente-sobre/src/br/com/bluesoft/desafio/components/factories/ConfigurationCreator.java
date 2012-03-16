package br.com.bluesoft.desafio.components.factories;

import org.hibernate.cfg.Configuration;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
@ApplicationScoped
public class ConfigurationCreator implements ComponentFactory<Configuration> {

    private final Configuration configuration;

    public ConfigurationCreator() {
        configuration = new Configuration();
    }

    public Configuration getInstance() {
        return configuration;
    }

}
