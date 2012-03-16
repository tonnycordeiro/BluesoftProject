package br.com.bluesoft.desafio.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "comment")
public class Comment {
	
	private Long id;
	private String description;
	private Date lastUpdate;
	private Theme theme;
	private Visitor visitor;

	public Comment() {
        this.lastUpdate = new Date();
    }
    
	@Id
	@GeneratedValue
	@Column(name = "COMMENT_ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "COMMENT_DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "COMMENT_LAST_UPDATE")
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH }, fetch=FetchType.EAGER)
    @JoinColumn(name = "THEME_ID") 	
	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH }, fetch=FetchType.EAGER)
    @JoinColumn(name = "VISITOR_ID") 	
	public Visitor getVisitor() {
		return visitor;
	}

	public void setVisitor(Visitor visitor) {
		this.visitor = visitor;
	}

    public void updateWithTheCurrentTimeAndDate(){
    	this.setLastUpdate(new Date());
    }

	@Override
	public String toString() {
		return "Comment [id=" + id + ", description=" + description
				+ ", lastUpdate=" + lastUpdate + ", theme=" + theme + ", visitor="
				+ visitor + "]";
	}
    
    
}
