package br.com.bluesoft.desafio.components.factories;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;


public class SessionFactoryCreatorTest {
	
	private SessionFactoryCreator sfcreator;
	private SessionFactory sessionFactory;

	@Before
	public void setUp() {
		sessionFactory = mock(SessionFactory.class);
		Configuration configuration = mock(Configuration.class);
		when(configuration.configure()).thenReturn(configuration);
		when(configuration.buildSessionFactory()).thenReturn(sessionFactory);
		
		sfcreator = new SessionFactoryCreator(configuration);
	}

	@Test
	public void shouldBuildSessionFactory() {
		sfcreator.buildSessionFactory();
		assertEquals(sessionFactory, sfcreator.getInstance());
	}
	
	@Test
	public void shouldCloseFactory() throws Exception {
		sfcreator.buildSessionFactory();
		sfcreator.closeFactory();
		verify(sessionFactory).close();
		
	}

}
