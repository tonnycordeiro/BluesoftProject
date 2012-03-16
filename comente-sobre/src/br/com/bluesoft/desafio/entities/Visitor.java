package br.com.bluesoft.desafio.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "visitor",
		uniqueConstraints = {@UniqueConstraint(columnNames={"VISITOR_EMAIL"})})
public class Visitor{
	
	private Long id;
	private String name;
	private String email;
	private List<Comment> comments;

	public Visitor(){
		comments= new ArrayList<Comment>();
	}

	public Visitor(String email){
		comments= new ArrayList<Comment>();
		this.email = email;
	}
	
	@Id
	@GeneratedValue
	@Column(name = "VISITOR_ID")
	public Long getId(){
		return id;
	}
	public void setId(Long id){
		this.id = id;
	}
	
	@Column(name = "VISITOR_NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "visitor", targetEntity = Comment.class)
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@Column(name = "VISITOR_EMAIL")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}	
	
	public void addComment(Comment comment) {
		this.comments.add(comment);
	}
	
	public void removeComment(Comment comment) {
		this.comments.remove(comment);
	}

	@Override
	public String toString() {
		return "Visitor [id=" + id + ", name=" + name + ", email=" + email + "]";
	}	
}
