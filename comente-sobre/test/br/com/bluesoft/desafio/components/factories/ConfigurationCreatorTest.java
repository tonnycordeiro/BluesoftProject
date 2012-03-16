package br.com.bluesoft.desafio.components.factories;

import static org.junit.Assert.*;

import org.hibernate.cfg.Configuration;
import org.junit.Test;

public class ConfigurationCreatorTest {

	@Test
	public void shouldCreateANewConfigurator() {
		ConfigurationCreator configurationCreator = new ConfigurationCreator();
		assertEquals(Configuration.class, configurationCreator.getInstance().getClass());
	}

}
