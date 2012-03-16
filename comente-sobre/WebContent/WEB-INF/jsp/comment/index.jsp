<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ page
	language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>

<jsp:include page="../defaults/tagHeadContent.jsp" />
<script type="text/javascript" src="/comente-sobre/js/project/comment/init.js"></script>
<script type="text/javascript" src="/comente-sobre/js/project/comment/especific.js"></script>

</head>
<body onloadeddata="decodeFieldsById(idFieldsToEncode)">
<div id="wrap">
	<jsp:include page="../defaults/header.jsp" />
	<div id="content">
		<span id="content-top"></span>	
		<div id="content-inner">
			<jsp:include page="editComment.jsp" />
			<jsp:include page="../defaults/errorMessage.jsp" />			

			<c:if test="${!empty standByComment}">
				<jsp:include page="standbyComment.jsp" />
			</c:if>
			<div class="clear"></div>
			<div id="homepageposts">
				<jsp:include page="listComments.jsp" />
			</div>
		</div>			
		<span id="content-bottom"></span>
	</div>
	<div id="main-nav">
	  <ul>
	    <li><a href="/comente-sobre/comment/other_theme"><img src="/comente-sobre/css/images/home.png" alt="" width="28" height="24" /></a></li>
	    <li class="active"><a href="#"><img src="/comente-sobre/css/images/blog.png" alt="" width="28" height="23" /></a></li>
	  </ul>
	</div>
	<div class="clear"></div>
	<div id="pagebottom"><jsp:include page="../defaults/footer.jsp" /></div>
</div>
</body>
</html>
