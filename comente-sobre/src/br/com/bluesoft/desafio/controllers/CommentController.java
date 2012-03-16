package br.com.bluesoft.desafio.controllers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;

import br.com.bluesoft.desafio.components.daos.CommentDao;
import br.com.bluesoft.desafio.components.daos.ThemeDao;
import br.com.bluesoft.desafio.components.daos.VisitorDao;
import br.com.bluesoft.desafio.components.utils.DataFromViewManager;
import br.com.bluesoft.desafio.components.utils.DataToViewManager;
import br.com.bluesoft.desafio.components.utils.DateManager;
import br.com.bluesoft.desafio.components.utils.PageManager;
import br.com.bluesoft.desafio.entities.Comment;
import br.com.bluesoft.desafio.entities.Theme;
import br.com.bluesoft.desafio.entities.Visitor;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;

@Resource
public class CommentController {

	private final CommentDao commentDao;
    private final VisitorDao visitorDao;
	private final ThemeDao themeDao;
    private final Validator validator;
    private final Result result;
    private int expirationTimeToEditCommetInSeconds;
    private int resultsByPage;
   
    public CommentController(CommentDao commentDao, ThemeDao themeDao, VisitorDao visitorDao, Validator validator, Result result) {
        this.visitorDao = visitorDao;
    	this.commentDao = commentDao;
        this.themeDao = themeDao;
        this.validator = validator;
        this.result = result;
        this.expirationTimeToEditCommetInSeconds = 600;
        this.resultsByPage = 5;
        commentDao.defineOrder("lastUpdate",false);
    }
    
	@Path("/{url}")
	public void index(String url, Boolean isTheViewConfigured) {
		if(isTheViewConfigured == null)
			isTheViewConfigured = false;
		
		if(!isTheViewConfigured && url != null && !url.trim().isEmpty())
			setInitialPagination( themeDao.loadThemeByURL(url) );		
	}
	
	@Post("/comment/filters")
	public void filterCommentsList(Theme theme, Comment standByComment, String firstDate,
							    	String lastDate,String exactTextIntoVisitorEmail,String exactTextIntoComment, 
							    	Integer currentPage){ 
		
    	loadAnShowTheStandbyComment(standByComment);
    	
		Date firstDatePT = null, lastDatePT = null;
		theme = themeDao.loadTheme(theme);
		
		exactTextIntoComment = DataFromViewManager.unescapeStringReturnedFromJavascriptMethodEscape(exactTextIntoComment);
		
		putFilersInTheView(firstDate,lastDate,exactTextIntoVisitorEmail,exactTextIntoComment);
		
		if(validateSearchFileds(firstDate,lastDate,theme)){
	    	if(firstDate!=null && lastDate!=null && !firstDate.trim().isEmpty() && !lastDate.trim().isEmpty()){
	    		try {
					firstDatePT = DateManager.parseStringToDate_PT_BR(firstDate);
					lastDatePT = DateManager.parseStringToDate_PT_BR(lastDate);					
				} catch (Exception e) {
					e.printStackTrace();
				}
	    		lastDatePT = DateManager.updateToTheLastSecondOfTheDay(lastDatePT);
	    	}
	        
	        setPaginationByFilters(theme, null, firstDatePT, 
					lastDatePT, exactTextIntoVisitorEmail, exactTextIntoComment,currentPage);
		}
	}

    private Comment addCommentIntoDB(Comment comment){
		Long idComment;
		idComment = commentDao.addComment(comment);
		if(idComment != null){
			comment.setId(idComment);
			comment = commentDao.loadComment(comment);
		}		
		
		return comment;
	}

	private void updateCommentIntoDB(Comment comment){
    	setAttributesAutomaticallyBeforeInsertionIntoDB(comment);
    	comment = commentDao.updateComment(comment);
	}

	private Visitor addNewVisitorByEmailIntoDB(Comment comment){
		Visitor visitor;
		String email;
		
		email = comment.getVisitor().getEmail();
	
		visitor = visitorDao.loadVisitorByEmail(email);
		
		if(visitor == null){
			visitor = new Visitor(email);
	    	visitor.setId(visitorDao.addVisitor(visitor));
	    	visitor = visitorDao.loadVisitor(visitor);
		}
	
		comment.setVisitor(visitor);
		return visitor;
	}

	@Post("/comment/edit")
    public void addNewComment(Comment comment){
    	if (receptAndValidateDatas(comment)){
	  		addNewVisitorByEmailIntoDB(comment);
	  		addCommentIntoDB(comment);
	    		
	        putCommentNotExpiredInStandBy(comment);
	        setInitialPagination(comment.getTheme());
    	}
    }
    
    @Put("/comment/edit")
    public void updateComment(Comment comment){
    	if(receptAndValidateDatas(comment)){
        	addNewVisitorByEmailIntoDB(comment);
        	
        	updateCommentIntoDB(comment);

        	putCommentNotExpiredInStandBy(comment);
        	setInitialPagination(comment.getTheme());
    	}
    }

    @Get
    @Path("/comment/edit")
    public void prepareCommentToEdition(Comment comment){
    	Theme theme;
    	comment = commentDao.loadComment(comment);
    	theme = themeDao.loadTheme(comment.getTheme());
    	putCommentToEdition(comment);
        setInitialPagination(theme);
    }

    @Delete
    @Path("/comment/edit")
    public void deleteComment(Comment comment){
    	Comment commentDeleted;
    	commentDeleted = commentDao.loadComment(comment);    	
    	Theme theme = themeDao.loadTheme(commentDeleted.getTheme());
    	commentDao.deleteComment(commentDeleted);
    	setInitialPagination(theme);
    }
    
    @Path("/comment/other_theme")
    public void redirectToNewThemeForm(){
        result.redirectTo(ThemeController.class).index();
    }       
    
    public Integer getExpirationTimeToEditCommetInSeconds() {
		return expirationTimeToEditCommetInSeconds;
	}    
    
	public void setExpirationTimeToEditCommetInSeconds(Integer timeInseconds) {
		this.expirationTimeToEditCommetInSeconds = timeInseconds;
	}

	public int getResultsByPage() {
		return resultsByPage;
	}

	public void setResultsByPage(int resultsByPage) {
		this.resultsByPage = resultsByPage;
	}
    
    private void configAndRedirectToPostsList(Theme theme, List<Comment> commentsList){
		theme = themeDao.loadTheme(theme);
		if(theme == null)
			redirectToNewThemeForm();
		else{
			putCommentsInTheView(commentsList);
			putThemeInTheView(theme);
			result.redirectTo(CommentController.class).index(theme.getUrl(),true);
		}		
	}

	private void unescapeFields(Comment comment) throws UnsupportedEncodingException{
		comment.getVisitor().setEmail(DataFromViewManager.unescapeStringReturnedFromJavascriptMethodEscape(comment.getVisitor().getEmail()));
		comment.setDescription(DataFromViewManager.unescapeStringReturnedFromJavascriptMethodEscape(comment.getDescription()));
	}

	private boolean validateSearchFileds(String firstDateStr, String lastDateStr, Theme theme){
		boolean isOk = true;
		String error;
		List<String> errors = new ArrayList<String>();
		
		if( 		(!firstDateStr.trim().isEmpty() && firstDateStr != null) && 
				   	(lastDateStr.trim().isEmpty() || lastDateStr == null)){
			isOk = false;
			errors.add(error = "Informar a data final do período");
			validator.add(new ValidationMessage(error,"Dados inválidos"));
		}
		
		if( 		(!lastDateStr.trim().isEmpty() && lastDateStr != null) && 
				   	(firstDateStr.trim().isEmpty() || firstDateStr == null)){
			isOk = false;
			errors.add(error = "Informar a data inicial do período");
			validator.add(new ValidationMessage(error,"Dados inválidos"));
		}
	
		if(!isOk)
			sendErrors(errors,"Favor consertar a(s) pendência(s) abaixo:", theme);
		return isOk;
	}

	private boolean validateTheme(Comment comment){
		Boolean isOk = true;
		String error;
		List<String> errors = new ArrayList<String>();
		
		if (comment.getDescription() == null || comment.getDescription().trim().isEmpty()){
			isOk = false;
			errors.add(error =  "O comentário não pode ser postado em branco");
			validator.add(new ValidationMessage(error,"Dados inválidos"));
		}
		if (comment.getVisitor().getEmail() == null || comment.getVisitor().getEmail().trim().isEmpty()){
			isOk = false;
			errors.add(error =  "Obrigatório informar um e-mail");
	        validator.add(new ValidationMessage(error,"Dados inválidos"));
		}
	
	    if(comment.getVisitor().getEmail() != null && !comment.getVisitor().getEmail().matches("^(.+)@(.+)")){
			isOk = false;
			errors.add(error =  "E-mail inválido");
	        validator.add(new ValidationMessage(error,"Dados inválidos"));
	    }
		
		if(!isOk){
	        sendErrors(errors,"Favor consertar a(s) pendência(s) abaixo:", comment.getTheme());
		}
		return isOk;
	}

	private boolean receptAndValidateDatas(Comment comment){
		try {
			unescapeFields(comment);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		comment.setTheme(themeDao.loadTheme(comment.getTheme()));
		return validateTheme(comment);
	}

	private void sendErrors(List<String> errors, String messageToVisitor, Theme theme){
    	String errorBlockHTML;
        errorBlockHTML = DataToViewManager.buildHtmlMessage(errors, messageToVisitor);
        result.include("showInputErrorsByHTML", errorBlockHTML);
        validator.onErrorRedirectTo(CommentController.class).index(theme.getUrl(),false);
    }

    private void setInitialPagination(Theme theme){
    	List<Comment> comments;
		Criteria criteriaCount, criteriaList;
	    criteriaCount = this.commentDao.createCriteriaToLoadCommentsByTheme(theme);
	    criteriaList = this.commentDao.createCriteriaToLoadCommentsByTheme(theme);
	    comments = generateCommentsListWithPagination(theme, criteriaCount, criteriaList, 1);

	    configAndRedirectToPostsList(theme, comments);
	}

	private void setPaginationByFilters(Theme theme, Visitor visitor,Date firstDate, Date lastDate, 
										String exactTextIntoVisitorEmail, String exactTextIntoComment, Integer currentPage){
		List<Comment> comments;
		Criteria criteriaCount, criteriaList;
	    criteriaCount = this.commentDao.createCriteriaByFilters(theme, null, firstDate, 
	    		lastDate, exactTextIntoVisitorEmail, exactTextIntoComment);
	
	    criteriaList = this.commentDao.createCriteriaByFilters(theme, null, firstDate, 
	    		lastDate, exactTextIntoVisitorEmail, exactTextIntoComment);
	
	    comments = generateCommentsListWithPagination(theme,criteriaCount,criteriaList,currentPage);
	    
	    configAndRedirectToPostsList(theme, comments);
	}

	private Integer createPageAndReturnFirstResult(long numberOfResults, int currentPage){
		PageManager page;
		Integer firstResult;
		int numberOfPages;
	
		page = new PageManager(this.resultsByPage,numberOfResults,currentPage);
		numberOfPages = page.countPages(numberOfResults);
		firstResult = page.defineFirstResult();
		
		putPaginationInTheView(numberOfPages, currentPage);
		
		return firstResult;
	}
    
    private List<Comment> generateCommentsListWithPagination(Theme theme, Criteria criteriaCount, Criteria criteriaList, Integer currentPage){
		long numberOfResults;
		Integer firstResult;
		List<Comment> comments = new ArrayList<Comment>();
	
	    numberOfResults = this.commentDao.countComments(criteriaCount);
	    firstResult = createPageAndReturnFirstResult(numberOfResults, currentPage);
	    
	    comments = this.commentDao.loadComments(criteriaList,this.resultsByPage,firstResult);
	
	    return comments;
	}

    private boolean IsExpiredTheTimeToEditComment(Comment comment){
		return (DateManager.differenceBetweenDatesInSeconds(comment.getLastUpdate(),new Date()/*now*/) > 
				this.expirationTimeToEditCommetInSeconds);
	}

	private void setAttributesAutomaticallyBeforeInsertionIntoDB(Comment comment){
		comment.updateWithTheCurrentTimeAndDate();
	}	

	private void putCommentsInTheView(List<Comment> comments){
		result.include("comments",comments);
	}    

	private void putThemeInTheView(Theme theme){
		result.include("themeId",theme.getId());
		result.include("themeName",theme.getName());
	}	
	
	private void putPaginationInTheView(long numberOfPages, int currentPage){
		result.include("numberOfPages",numberOfPages);
		result.include("currentPage",currentPage);
	}    

	private void putFilersInTheView(String firstDate, String lastDate, String exactTextIntoVisitorEmail, String exactTextIntoComment){
		result.include("firstDate",firstDate);
		result.include("lastDate",lastDate);
		result.include("exactTextIntoVisitorEmail",exactTextIntoVisitorEmail);
		result.include("exactTextIntoComment",exactTextIntoComment);
	}
	
	private void  loadAnShowTheStandbyComment(Comment standByComment){
		standByComment = commentDao.loadComment(standByComment);
		if(standByComment != null)
			putCommentNotExpiredInStandBy(standByComment);
	}

	private void putCommentNotExpiredInStandBy(Comment comment){
		if(!IsExpiredTheTimeToEditComment(comment)){
			result.include("standByComment",comment);
			result.include("standByTime",(Integer)expirationTimeToEditCommetInSeconds/60);
		}
		result.include("commentToEdition",null);
	}

	private void putCommentToEdition(Comment comment){
		if(!IsExpiredTheTimeToEditComment(comment)){
			result.include("commentToEdition",comment);
		}
		result.include("standByComment",null);
	}	

}
