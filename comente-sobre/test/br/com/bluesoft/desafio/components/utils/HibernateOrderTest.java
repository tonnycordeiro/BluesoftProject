package br.com.bluesoft.desafio.components.utils;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class HibernateOrderTest {
	private Criteria c1;
	private Criteria c2;
	private HibernateOrder order;
	
	@Before
	public void setUp(){
		order = new HibernateOrder("a field", true);
		c1 = mock(Criteria.class);
		c2 = mock(Criteria.class);
		when(c1.addOrder((Order) any())).thenReturn(c2);
	}

	@Test
	public void shouldhaveAField(){
		order = new HibernateOrder();
		String field = "a field";
		order.setField(field);
		assertEquals(field, order.getField());
	}

	@Test
	public void shouldToBeAsc(){
		order = new HibernateOrder();
		boolean isAsc = true;
		order.setAsc(isAsc);
		assertEquals(isAsc, order.isAsc());
	}


	@Test
	public void shouldToBeDesc(){
		order = new HibernateOrder();
		boolean isAsc = false;
		order.setAsc(isAsc);
		assertEquals(isAsc, order.isAsc());
	}

    @Test
    public void shouldConvertToStringWithMainAttributes(){
    	String strOrder = order.toString();
    	assertTrue(strOrder.contains(order.getField().toString()));
    }
	
	@Test
	public void shouldAddDefaultAscOrder(){
		Criteria c3;
		order.setAsc(true);
		c3 = order.addDefaultOrder(c1);
		verify(c1).addOrder((Order) any());
		assertSame(c3,c2);
	}

	@Test
	public void shouldAddDefaultDescOrder(){
		Criteria c3;
		order.setAsc(false);
		c3 = order.addDefaultOrder(c1);
		verify(c1).addOrder((Order) any());
		assertSame(c3,c2);
	}
	
	
}

