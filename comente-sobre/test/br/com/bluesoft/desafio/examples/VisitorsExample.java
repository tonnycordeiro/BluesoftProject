package br.com.bluesoft.desafio.examples;

import br.com.bluesoft.desafio.entities.Visitor;

public class VisitorsExample {
    public static Visitor createVisitor(String name,Long id,String email) {
    	Visitor visitor = new Visitor();
    	visitor.setId(id);
    	visitor.setName(name);
    	visitor.setEmail(email);
        return visitor;
    }
}
