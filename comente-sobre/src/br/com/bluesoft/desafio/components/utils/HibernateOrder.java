package br.com.bluesoft.desafio.components.utils;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

public class HibernateOrder {
	
	private String field;
	private boolean isAsc;

	public HibernateOrder(){
	}	
	
	public HibernateOrder(String field,boolean isAsc){
		this.field = field;
		this.isAsc = isAsc; 
	}
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public boolean isAsc() {
		return isAsc;
	}

	public void setAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}
	
	public Criteria addDefaultOrder(Criteria criteria){
		if(this.field != null)
			if(this.isAsc)
				return criteria.addOrder(Order.asc(this.field));
			else
				return criteria.addOrder(Order.desc(this.field));
	
		/*without order*/
		return criteria;
	}

	@Override
	public String toString() {
		return "HibernateOrder [field=" + field + ", isAsc=" + isAsc + "]";
	}
}
