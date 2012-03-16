package br.com.bluesoft.desafio.components.factories;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import org.hibernate.classic.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;

public class SessionCreatorTest {
	
	private SessionFactory sessionFactory;
	private SessionCreator sessionCreator;

	@Before
	public void setUp() {
		sessionFactory = mock(SessionFactory.class);
		sessionCreator = new SessionCreator(sessionFactory);
	}

	@Test
	public void shouldOpenSession() {

		sessionCreator.openSession();
		verify(sessionFactory).openSession();
	}
	
	@Test
	public void shouldCloseSession() throws Exception {
		Session session = mock(Session.class);
		when(sessionFactory.openSession()).thenReturn(session);
		sessionCreator.openSession();
		sessionCreator.closeSession();
		verify(session).close();	
	}
	
	@Test
	public void shouldReturnSession() throws Exception {
		Session session = mock(Session.class);
		when(sessionFactory.openSession()).thenReturn(session);
		assertEquals(session, sessionCreator.getInstance());
	}

}
