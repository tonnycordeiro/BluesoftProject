<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="aboutme-full" style="margin-left:10px;">
<div align="center"><h2>${themeName}</h2></div>
	<form id="formNewComment" name="formNewComment" action="/comente-sobre/comment/edit" method="post">
		<input type="hidden" name="comment.theme.id" value="${themeId}"/>
		<input type="hidden" name="comment.theme.name" value="${themeName}"/>
		<input type="hidden" name="comment.id" value="${commentToEdition.id}"/>			
		e-mail:
			<input type="text" id="emailField" class="field" size="27" value="${commentToEdition.visitor.email}"
			onblur="checkEmail(this.value,'msgEmail')"
			onkeypress="activateButtonOfTheTextInputByEnterKey(event,'btPUT')" />
			<input type="hidden" id="email" name="comment.visitor.email" value="${commentToEdition.visitor.email}"/>
			<span id="msgEmail" style="color:red"></span>
		<br/>
		comentário:
		<br/>
					<textarea id="descriptionField" class="field" rows="6" cols="70">${commentToEdition.description}</textarea>
					<input type="hidden" id="description" name="comment.description" value="${commentToEdition.description}"/>
		<br/>
		<c:choose>
			<c:when test="${empty commentToEdition}">
				<input type="button" id="btPUT" value="   postar   " onclick="validateForm('formNewComment',false)">
			</c:when>
			<c:otherwise>
				<input type="button" id="btPUT" name="btPUT" value="SALVAR" onclick="validateForm('formNewComment',true)">
			</c:otherwise>
		</c:choose>
	</form>
	<div class="clear"></div>
</div>
