/* pre-requisites:
 * commons-validator-1.4.0.js
 * jquery-1.7.1.js
 * jquery-ui.js
 * jquery.window.js
 * jquery.window.css
 * jquery-ui.css 
	*/

function checkEmail(email,idTextPlace){
	if(!jcv_checkEmail(email))
		document.getElementById(idTextPlace).innerHTML = "e-mail inv&aacute;lido";
	else
		document.getElementById(idTextPlace).innerHTML = "";
}

function alertWindow(alertText){
 	$.window({
		   title: "Alerta",
		   content: "<div style='padding:10px; font-weight:bold;'>"+alertText+"</div>",
		   draggable: false,
		   resizable: false,
		   maximizable: false,
		   minimizable: false,
		   showModal: true
		});	
 }

function changeRESTbehaviorToPUT(idButton){
	document.getElementById(idButton).setAttribute("type","submit");
	document.getElementById(idButton).setAttribute("name","_method");
	document.getElementById(idButton).setAttribute("value","PUT");
}

function buildMessageWithMarkers(marker,arrayTopics){
	var message = "";
	for(var i=0;i<arrayTopics.length;i++)
		message = message + marker + arrayTopics[i];
	return message;
}

function formatToBoldTheStringWordsIfItExistsInTheMainPhrase(mainPhrase,stringToFormat,idLocalToPress){
	var i;
	var arrayMainWords;
	var regExpr;
	stringToFormat = new String(stringToFormat);
	mainPhrase = new String(mainPhrase);
	
	arrayMainWords = mainPhrase.split(" ");
	for(i=0;i<arrayMainWords.length;i++){
		regExpr = new RegExp(arrayMainWords[i],"ig");
		stringToFormat = stringToFormat.replace(regExpr,"<b>"+arrayMainWords[i]+"</b>");
	}
	document.getElementById(idLocalToPress).innerHTML = stringToFormat;
 }

