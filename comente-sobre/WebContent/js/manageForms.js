function activateButtonOfTheTextInputByEnterKey(event,idButton){
	if((window.event ? event.keyCode : event.which) == 13)
		eval(document.getElementById(idButton).onclick());
}

function encodeFieldsById(arrayIdFields){
	for (var i=0;i<arrayIdFields.length;i++)
		document.getElementById(arrayIdFields[i]).value = escape(document.getElementById(arrayIdFields[i]).value);
}

function decodeFieldsById(arrayIdFields){
	for (var i=0;i<arrayIdFields.length;i++)
		document.getElementById(arrayIdFields[i]).value = unescape(document.getElementById(arrayIdFields[i]).value);
}

function changeToNextField(idCurrentField,idNextField){
	var currentField = document.getElementById(idCurrentField);
	var nextField = document.getElementById(idNextField);
	
	if (currentField.value.length == currentField.maxLength || currentField.type == 'select-one' || currentField.type == 'checkbox')
		nextField.focus();	 
}

function encodeValueInOtherField(idOutField,idInField){
	document.getElementById(idInField).value = escape(document.getElementById(idOutField).value);
}

function isEmptyField(str){
	if (str == null)
		return true;
	if (str.replace(/ /g,"") == "")
		return true;
	return false;
}

/*arrayRequiredFields has two fields: "input id" and "field name to show for user " */
/*areFilledTheRequiredFields*/ 
function getNotFilledRequiredFields(arrayRequiredFields){
	var requiredFields = new Array();
	for (var i=0;i<arrayRequiredFields.length;i++){
		if(isEmptyField(document.getElementById(arrayRequiredFields[i][0]).value)) /*requiredField =*/ 
			requiredFields.push(arrayRequiredFields[i][1]);
	}
	return requiredFields;
}

function returnEmptyFields(arrayFields){
	var strAlerts = "";
	for (var i=0;i<arrayFields.length;i++){
		if(isEmptyField(document.getElementById(arrayFields[i][0]).value))
			strAlerts += "\n *  " + arrayFields[i][1];
	}
	return strAlerts;
}

