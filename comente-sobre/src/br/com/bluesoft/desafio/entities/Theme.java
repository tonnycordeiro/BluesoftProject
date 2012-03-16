package br.com.bluesoft.desafio.entities;

import java.text.Normalizer;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TokenizerDef;
import org.apache.solr.analysis.StandardTokenizerFactory;

@Entity
@Table(name = "theme",
		uniqueConstraints = {@UniqueConstraint(columnNames={"THEME_NAME","THEME_URL"})})
@Indexed
@AnalyzerDef(name = "customanalyzer",
			 tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class))
public class Theme{

	private Long id;
	private String name;
	private String url;
	private Date creationDate;
	private List<Comment> comments = null;
	
	public Theme() {
		this.comments = new ArrayList<Comment>(); 
		this.creationDate = new Date();
	}

	@Id
	@GeneratedValue
	@Column(name = "THEME_ID")
	@DocumentId
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "THEME_NAME")
	@Analyzer(definition = "customanalyzer")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "THEME_URL")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}	
	
	@Column(name = "THEME_DATE")
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@OneToMany(mappedBy = "theme", targetEntity = Comment.class)
	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public void addComment(Comment comment) {
		this.comments.add(comment);
	}
	
	public void removeComment(Comment comment) {
		this.comments.remove(comment);
	}

	public void nameToUpper(){
		this.name = this.name.toUpperCase();
	}

	public String createAndSetUrlByThemeName(){
		String patternOfNotReservedSymbolsOfURL = "\\p{Alnum}-_.~";
		if(this.name == null)
			return null;
		this.url = this.name.replace(' ','-').toLowerCase(); 
		url = Normalizer.normalize(url, Normalizer.Form.NFD);
		return url.replaceAll("[^"+patternOfNotReservedSymbolsOfURL+"]", "");
	}
	
	@Override
	public String toString() {
		return "Theme [id=" + id + ", name=" + name + ", url=" + url
				+ ", creationDate=" + creationDate + "]";
	}
}
