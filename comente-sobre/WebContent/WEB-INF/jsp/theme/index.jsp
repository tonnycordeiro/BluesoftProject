<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../defaults/tagHeadContent.jsp" />
<script type="text/javascript" src="/comente-sobre/js/project/theme/init.js"></script>
<script type="text/javascript" src="/comente-sobre/js/project/theme/especific.js"></script>

</head>
<body>
<div id="wrap">
	<jsp:include page="../defaults/header.jsp" />
	<div id="content">
		<span id="content-top"></span>	
		<div id="content-inner">
			<jsp:include page="newTheme.jsp" />
			<jsp:include page="themesSuggestions.jsp" />
			<jsp:include page="../defaults/errorMessage.jsp" />
		
			<c:if test="${!empty suggestionsThemes}">
				<script type="text/javascript">
					createWindowSuggestionsThemes();
				</script>
			</c:if>
		</div>
		<span id="content-bottom"></span>
	</div>

  <div id="main-nav">
    <ul>
      <li class="active"><a href="/comente-sobre/"><img src="/comente-sobre/css/images/home.png" alt="" width="28" height="24" /></a></li>
      <li class="inactive"><a href="#"><img src="/comente-sobre/css/images/blog.png" alt="" width="28" height="23" /></a></li>
    </ul>
  </div>
  <div class="clear"></div>
  <div id="pagebottom"><jsp:include page="../defaults/footer.jsp" /></div>
</div>
	
</body>

</html>
												