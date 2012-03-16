var requiredFields = new Array(["candidateThemeField","assunto"]);
var idFieldsToEncode = ['candidateTheme']; 

function createWindowSuggestionsThemes() {
	$.window({
		showModal: true,
		modalOpacity: 0.5,
		title: "Temas semelhantes",
		content: $("#winSuggestionsThemes").html()
	});
}

