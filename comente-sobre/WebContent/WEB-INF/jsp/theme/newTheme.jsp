<div id="header">
<div id="aboutme">
<form id="formNewTheme" name="formNewTheme" action="/comente-sobre/theme/try_add" method="post">

	<h2>Sobre o quê você deseja comentar
						<font size="7">?</font>
						<font size="-1">?</font>
						<font size="6">?</font>
						<font size="-8">?</font>
						<font size="1">?</font>
						<font size="-2">?</font>
						<font size="10">?</font>
						<font size="-8">?</font>
	</h2>
	<input type="text" id="candidateThemeField" maxlength="200" size="35" value="${newTheme.name}"
	onkeypress="activateButtonOfTheTextInputByEnterKey(event,'btSubmitFormNewTheme')"
	/>
	<input type="hidden" id="candidateTheme" name="candidateTheme.name" maxlength="200" size="50" value="${newTheme.name}"/>
	<span id="ps-field">digite o assunto ou palavra chave</span>
	<div align="center">
		<input type="button" id="btSubmitFormNewTheme" value="    comente    " onclick="validateForm('formNewTheme')">
	</div>
	<div class="clear"></div>
</form>
</div>
</div>