<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="winSuggestionsThemes" style="display:none;">
   <div style="padding:10px;">
      <h2><a href="#">Seja o primeiro a comentar:</a></h2>
      <div>
		<c:if test="${!empty suggestionsThemes}">
		<form id="formConfirmAddTheme" name="formConfirmAddTheme" action="/comente-sobre/theme/add" method="post">
			<table>
				<tr>
					<td>
						${newTheme.name}
						<input type="hidden" id="newTheme" name="newTheme.name" maxlength="200" size="50" value="${newTheme.name}"/>
						<input type="button" value="   comente   " onclick="validateForm('formConfirmAddTheme')"> 
					</td>
				</tr> 
			</table>
		</form>
		
		<div id="list" style="margin-top:20px">
	        <h2><a href="#">Ou verifique se o assunto já existe:</a></h2>
			<p>Postagens anteriores</p>
			<ul>
			<c:forEach items="${suggestionsThemes}" var="theme">
			<li>
				<a href="/comente-sobre/theme/choosen?theme.id=${theme.id}">
				<span id="theme_${theme.id}"></span>
				<script type="text/javascript">
					formatToBoldTheStringWordsIfItExistsInTheMainPhrase("${newTheme.name}","${theme.name}","theme_${theme.id}");
				</script>
					&lt;&lt;&lt; veja o que já falaram !!
				</a>
			</li>
			</c:forEach>
			</ul>
			
		</div>
		</c:if>

		
      </div>
   </div>
</div>
