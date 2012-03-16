function createWindowSuggestionsThemes() {
	$.window({
		showModal: true,
		modalOpacity: 0.5,
		title: "Temas semelhantes",
		content: $("#winSuggestionsThemes").html()
	});
}

function buildMessageForInputErrors(){
	fieldsNotFilled = getNotFilledRequiredFields(requiredFields);
	strAlert = buildMessageWithMarkers("<br/> *   ",fieldsNotFilled);
	
	return strAlert; 
}

function validateForm(idForm){
	var strAlert = "";
	switch(idForm){
		case "formNewTheme":
			/* Do not show encoding for user */
			encodeValueInOtherField('candidateThemeField','candidateTheme');
			strAlert = buildMessageForInputErrors();
			break;
		case "formConfirmAddTheme":
			encodeFieldsById(idFieldsToEncode.concat(new Array(['newTheme'])));
			break;
		default:
			break;
	}
 	if (strAlert != "")
 		alertWindow("Preencha os campos abaixo:" + strAlert);
 	else
 		document.getElementById(idForm).submit();
} 
