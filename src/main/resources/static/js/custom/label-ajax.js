// Normal
var createLabelInputId = "#create-lbl-input";
var createLabelBtnId = "#create-lbl-btn";
var labelContainerId = "#label-radio-container";
var labelContainerClass = ".label-radio-container";
var selectLabelContainerId = "#label-select-container";
var deleteLabelBtnClass = ".delete-label";

var labelSelect = "label-select";
// // Modal
var createLabelModalInputId = "#create-lbl-modal-input";
var createLabelModalBtnId = "#create-lbl-modal-btn";
var labelModalContainerId = "#label-radio-modal-container";

// Links
var labelUri = MY_APP.contextPath + 'api/labels';

$(document).ready(function () {
	/*
	*   CREATE
	* */
	$(createLabelBtnId).click(function(e) {
		e.preventDefault(e);
		var input = $(createLabelInputId);
		createLabel(input);
	})

	// Modal
	$(createLabelModalBtnId).click(function(e) {
		e.preventDefault(e);
		var input = $(createLabelModalInputId);
		createLabel(input);
	})

	/*
	*   DELETE
	* */
	$(labelContainerClass).on("click", deleteLabelBtnClass, function(e) {
		e.preventDefault(e);
		processDelete($(this));
	});
});

function createLabel(input) {
	resetLabelForm(input);

	var data = {
		title: input.val()
	};

	$.ajax({
		url: labelUri,
		method: "post",
		data: JSON.stringify(data),
		contentType: "application/json; charset=utf-8",
		dataType: "json"
	}).done(function (data) {
		handleCreateSuccess(input, data);
	}).fail(function (jqXHR, textStatus, errorThrown) {
		handleCreateFail(input, jqXHR, textStatus, errorThrown);
	});
}
/*
*   CREATE
* */
function resetLabelForm(input) {
	// reset
	input.removeClass("is-invalid");
	$(".invalid-feedback").remove();
}

function handleCreateSuccess(input, data) {
	// If successful
	var id = data.id;
	var title = data.title;
	var template = generateLabelTemplate(id, title);
	$(labelContainerId).append(template);

	template = replaceLabelModal(input, template, id); // label_id => label_id_modal
	$(labelModalContainerId).append(template);

	var selectTemplate =
		"<option class=\""+ labelSelect +"\" value=\""+ id +"\">"+ title +"</option>";
	$(selectLabelContainerId).append(selectTemplate);

	createToast("CREATE LABEL",
		"success",
		"New label has just been created successfully !");
	// reset
	$(createLabelInputId).val("");
	$(createLabelModalInputId).val("");
}

function handleCreateFail(input, jqXHR, textStatus, errorThrown) {
	// If fail
	var res = jqXHR.responseJSON;
	var errorsArr = res.errors;
	createToast("CREATE LABEL ("+ res.error +")",
		"danger",
		"Create unsuccessfully : there must be some error !");

	var content = errorsArr[0].defaultMessage;
	var errTemplate =
		"<div class=\"invalid-feedback\">" +
		content +
		"</div>";
	input.addClass("is-invalid");
	input.parent().append(errTemplate);
}

/*
*   DELETE
* */
function processDelete(el) {
	var id = el.attr("data-id");

	$.ajax({
		url: labelUri + "/" + id,
		method: "delete",
		dataType: "json"
	}).done(function (data) {
		$(".label-item[value='"+ id +"']").parent().fadeOut("fast", function() {
			$(".label-item[value='"+ id +"']").parent().remove();
		});

		$("." + labelSelect + "[value='"+ id +"']").remove();
		createToast("DELETE LABEL",
			"success",
			"Label has just been delete successfully!");
	}).fail(function (jqXHR, textStatus, errorThrown) {
		var res = jqXHR.responseJSON;
		var content = res.message + "<br>Path : " + res.path;
		createToast("DELETE LABEL ("+ res.error +")",
			"danger",
			"Create unsuccessfully : " + content);
	});
}

/*
*   extends functions
* */
function generateLabelTemplate(id, title) {
	var template =
		"<div class=\"custom-control custom-radio mt-1\">" +
		"<input type=\"radio\" name=\"label\" id=\"label_"+ id +"\" "
		+ "class=\"custom-control-input label-item\" value=\"" + id + "\">" +
		"<label class=\"custom-control-label\" for=\"label_"+ id +"\">" + title + "</label>" +
		"<a class=\"delete-label\" data-id=\"" + id + "\">&times;</a>" +
		"</div>";
	return template;
}

function replaceLabelModal(input, template, id) {
	var newText = "label_"+ id + "_modal";
	return template.replace(/label_\d+/g, newText);
}