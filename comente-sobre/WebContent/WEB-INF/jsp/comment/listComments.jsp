<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<form id="searchForm" name="searchForm" method="post" action="/comente-sobre/comment/filters">
	<input type="hidden" name="standByComment.id" value="${standByComment.id}">
	<input type="hidden" name="theme.id" value="${themeId}"/>
	<input type="hidden" name="theme.name" value="${themeName}"/>
	<input type="hidden" name="currentPage" id="currentPage" value="${currentPage}"/>	
	<div id="searchList" style="margin-left:20px">
		<table>
			<thead>
				<tr>
					<th align="center">Data</th>
					<th align="center">E-mail</th>
					<th align="center">Comentário</th>
					<th>
						<input type="button" class="button-search" onclick="clearFields();changeCurrentPage(1);validateForm('searchForm',false)" value="listar todos"/>
					</th>
				</tr>
				<tr>
					<th nowrap="nowrap">
						<input type="text" name="firstDate" size="7" id="firstDate" value="${firstDate}" /> a
						<input type="text" name="lastDate" size="7" id="lastDate" value="${lastDate}" />
					</th>
					<th>
						<input type="text" name="exactTextIntoVisitorEmail" id="exactTextIntoVisitorEmail" value="${exactTextIntoVisitorEmail}"/>
					</th>
					<th>
						<input type="text" name="exactTextIntoCommentField" id="exactTextIntoCommentField" value="${exactTextIntoComment}"/>
						<input type="hidden" name="exactTextIntoComment" id="exactTextIntoComment" value="${exactTextIntoComment}"/>
					</th>
					<th>
						<input type="button" class="button-search" onclick="changeCurrentPage(1);validateForm('searchForm',false)" value="filtrar"/>
					</th>
				</tr>							
			</thead>
		</table>
	</div>
			
	<c:forEach items="${comments}" var="com">
        <div class="post">
          <div class="datebox"> 
          	<span class="date">
          		${com.lastUpdate.getDate()}
          	</span>  
          	<span class="month_year">
          		${com.lastUpdate.getMonth()+1}/${com.lastUpdate.getYear()+1900}
          	</span> </div> 
          <div class="postcontent"><span class="commentsbox"></span> 
            <h2>${com.visitor.email}</h2>
            <p>${com.description}</p>
          </div>
          <div class="clear"></div>
        </div>
	</c:forEach>
	
	<div class="pagination">
        <div class="wp-pagenavi">
			<c:forEach var="i" begin="1" end="${numberOfPages}" step="1" varStatus ="status">
				<c:choose>
					<c:when test="${i == currentPage}">
						<span class="current">${currentPage}</span>
					</c:when>
					<c:otherwise>
						<a href="#" onclick="changeCurrentPage(${i})">${i}</a>
					</c:otherwise> 
				</c:choose>
			</c:forEach>
		</div>
    </div>	
	

		
</form>
		
