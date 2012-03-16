package br.com.bluesoft.desafio.examples;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.bluesoft.desafio.entities.Comment;

public class CommentsExample {
    public static Comment createComment(String description,Long id,Date lastUpdate) {
    	Comment comment = new Comment();
    	comment.setId(id);
        comment.setDescription(description);
        comment.setLastUpdate(lastUpdate);
        
        return comment;
    }
    
    public static Comment[] createArrayWithTwoComments(){
    	Comment[] comments = new Comment[2];
    	comments[0] = createComment("a description 1",1L,new Date());
    	comments[1] = createComment("a description 2",2L,new Date());
    	return comments;
    }

    public static Comment[] createArrayWithOneComment(){
    	Comment[] comments = new Comment[1];
    	comments[0] = createComment("a description 1",1L,new Date());
    	return comments;
    }

    public static List<Comment> createListWithTwoComments(){
    	List<Comment> commentsList = new ArrayList<Comment>();
    	commentsList.add(createComment("a description 1",1L,new Date()));
    	commentsList.add(createComment("a description 2",2L,new Date()));    	
    	return commentsList;
    }    
}
