function validateForm(idForm,isToUpdate){
	var fieldsNotFilled;
	var strAlert = "";
	
	switch(idForm){
		case "formNewComment":
			if(!jcv_checkEmail(document.getElementById("emailField").value)){
				alertWindow("e-mail inv&aacute;lido");
				return;
			}
			/*purpose: Do not show encoding for user */
			encodeValueInOtherField('emailField','email');
			encodeValueInOtherField('descriptionField','description');
			
			
			fieldsNotFilled = getNotFilledRequiredFields(requiredFields);
			strAlert = buildMessageWithMarkers("<br/> *   ",fieldsNotFilled);
			if(isToUpdate){
				if(fieldsNotFilled.length > 0){
				 	if (strAlert != "")
				 		alertWindow("Preencha os campos abaixo:" + strAlert);
					location.href='/comente-sobre/comment/edit?comment.id=${commentToEdition.id}';
				}else{
					changeRESTbehaviorToPUT("btPUT");
					return;
				}
			}
			break;
		case "searchForm":
			/*purpose: Do not show encoding for user */
			encodeValueInOtherField('exactTextIntoCommentField','exactTextIntoComment');			
			break;
		default:
			break;
	}
 	if (strAlert != "")
 		alertWindow("Preencha os campos abaixo:" + strAlert);
 	else
 		document.getElementById(idForm).submit();
} 

function changeCurrentPage(page){
	document.getElementById("currentPage").value = page;
	validateForm("searchForm",false);
}

function clearFields(){
	document.getElementById("exactTextIntoVisitorEmail").value = "";
	document.getElementById("exactTextIntoComment").value = "";
	document.getElementById("exactTextIntoCommentField").value = "";
	document.getElementById("firstDate").value = "";
	document.getElementById("lastDate").value = "";
}

