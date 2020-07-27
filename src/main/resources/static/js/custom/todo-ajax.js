// Normal
var createTodoInputId = "#create-todo-input";
var createTodoBtnId = "#create-todo-btn";

// Modal
var createTodoModalInputId = "#create-todo-modal-input";
var createTodoModalBtnId = "#create-todo-modal-btn";

var labelContainerId = "#label-radio-container";
var labelModalContainerId = "#label-radio-modal-container";
var todoTableHeadingId = "#todo-table-heading";
var allOfTodosContainerId = "#all-of-todos-container";
var selectLabelContainerId = "#label-select-container";

// Links
var todoUri = MY_APP.contextPath + 'api/todos';
var labelUri = MY_APP.contextPath + 'api/labels';

$(document).ready(function () {
	/*
	*   CREATE
	* */
	$(createTodoBtnId).click(function(e) {
		e.preventDefault(e);
		createTodo(createTodoInputId, labelContainerId);
	})

	// Modal
	$(createTodoModalBtnId).click(function(e) {
		e.preventDefault(e);
		createTodo(createTodoModalInputId, labelModalContainerId);
	})

	$(selectLabelContainerId).on("change", function(e) {
		var labelId = $(".label-select:selected").val();
		if(labelId != 'null')
			renderTodoList(labelId);
		else
			renderRecentTodoList();
	});

});

function renderRecentTodoList() {
	$.ajax({
		url: todoUri + "?withtask=true",
		method: "get",
		contentType: "application/hal+json; charset=utf-8",
		dataType: "json"
	}).done(function (data) {
		$(todoTableHeadingId).html(""); // reset

		console.log(data);
		var todos = data.content;
		$.each(todos, function( index, value ) {
			var template = generateTodoTemplate(value);
			$(todoTableHeadingId).append(template);
		});
	}).fail(function (jqXHR, textStatus, errorThrown) {
		console.log(jqXHR);
		createToast("Filter Error",
			"danger",
			textStatus + " : " + errorThrown);
	});
}

function renderTodoList(labelId) {
	$.ajax({
		url: labelUri + "/" + labelId,
		method: "get",
		contentType: "application/hal+json; charset=utf-8",
		dataType: "json"
	}).done(function (data) {
		$(todoTableHeadingId).html(""); // reset

		console.log(data);
		var todos = data.todos;
		$.each(todos, function( index, value ) {
			var template = generateTodoTemplate(value);
			$(todoTableHeadingId).append(template);
		});
	}).fail(function (jqXHR, textStatus, errorThrown) {
		console.log(jqXHR);
		createToast("Filter Error",
			"danger",
			textStatus + " : " + errorThrown);
	});
}

function createTodo(input, labelContainer) {
	resetLabelForm(input);
	var labelId = $(labelContainer + " input[name='label']:checked").val();
	if(labelId === undefined) {
		// quăng lỗi ngay
		createToast("CREATE TODO",
			"danger",
			"Create unsuccessfully : must select label !");
		var content = "must select label !"
		var errTemplate =
			"<div class=\"invalid-feedback\">" +
			content +
			"</div>";
		$(input).addClass("is-invalid");
		$(input).parent().append(errTemplate);
		return;
	}
	var data = {
		title: $(input).val(),
		labelId: labelId
	};

	$.ajax({
		url: todoUri,
		method: "post",
		data: JSON.stringify(data),
		contentType: "application/json; charset=utf-8",
		dataType: "json"
	}).done(function (data) {
		console.log(data);
		handleCreateTodoSuccess(input, data, labelId);
	}).fail(function (jqXHR, textStatus, errorThrown) {
		console.log(jqXHR);
		handleCreateTodoFail(input, jqXHR, textStatus, errorThrown);
	});
}
/*
*   CREATE
* */
function resetLabelForm(input) {
	// reset
	$(input).removeClass("is-invalid");
	$(".invalid-feedback").remove();
}

function handleCreateTodoSuccess(input, data, labelId) {
	// If successful
	var selectId = $(".label-select:selected").val();
	if(selectId == labelId || selectId == 'null') {
		var template = generateTodoTemplate(data);
		$(todoTableHeadingId).append(template);
	}

	var allOfTodoItemTemplate = generateTodoItemTemplate(data);
	$(allOfTodosContainerId).append(allOfTodoItemTemplate);

	createToast("CREATE TODO",
		"success",
		"New todo has just been created successfully !");

	// reset
	$(createTodoInputId).val("");
	$(createTodoModalInputId).val("");
}

function handleCreateTodoFail(input, jqXHR, textStatus, errorThrown) {
	// If fail
	var res = jqXHR.responseJSON;
	var errorsArr = res.errors;
	createToast("CREATE TODO ("+ res.error +")",
		"danger",
		"Create unsuccessfully : there must be some error !");

	var content = errorsArr[0].defaultMessage;
	var errTemplate =
		"<div class=\"invalid-feedback\">" +
		content +
		"</div>";
	$(input).addClass("is-invalid");
	$(input).parent().append(errTemplate);
}

/*
*   DELETE
* */
// function processTodoDelete(el) {
// 	var id = el.attr("data-id");
//
// 	$.ajax({
// 		url: todoUri + "/" + id,
// 		method: "delete",
// 		dataType: "json"
// 	}).done(function (data) {
// 		$("#label_" + id).parent().fadeOut();
// 		$("." + labelSelect + "[value='"+ id +"']").remove();
// 		createToast("DELETE LABEL",
// 			"success",
// 			"Label has just been delete successfully!");
// 	}).fail(function (jqXHR, textStatus, errorThrown) {
// 		var res = jqXHR.responseJSON;
// 		var content = res.message + "<br>Path : " + res.path;
// 		createToast("DELETE LABEL ("+ res.error +")",
// 			"danger",
// 			"Create unsuccessfully : " + content);
// 	});
// }

/*
*   extends functions
* */
function generateTodoTemplate(data) {
	var id = data.id;
	var title = data.title;
	var taskSize = data.tasks !== null ? data.tasks.length : 0;
	var template =
	"<tr>\n"
		+ "\t\t\t<th scope=\"row\">"+ id +"</th>\n"
		+ "\t\t\t<td>Todo : <a href=\"javascript:void(0)\" data-id=\""+ id +"\"\n"
		+ "\t\t\t              class=\"todo-title link-hover trigger-task-modal\">\n"
		+ title
		+ "\t\t\t</a></td>\n"
		+ "\t\t\t<td class=\"todo-size_"+ id +"\">"+ taskSize +"</td>\n"
		+ "\t\t</tr>"
	return template;
}

function generateTodoItemTemplate(data) {
	var id = data.id;
	var title = data.title;
	var template =
		"<div class=\"col-12 col-md-6 col-lg-4\">\n"
		+ "\t\t\t\t<div class=\"media block-6 services d-block\">\n"
		+ "\t\t\t\t\t<div class=\"icon\">\n"
		+ "\t\t\t\t\t\t<span class=\"flaticon-video-camera\"></span>\n"
		+ "\t\t\t\t\t</div>\n"
		+ "\t\t\t\t\t<div class=\"media-body\">\n"
		+ "\t\t\t\t\t\t<h3 class=\"heading mb-3\">\n"
		+ "\t\t\t\t\t\t\tTodo : <a href=\"javascript:void(0)\"\n"
		+ "\t\t\t\t\t              class=\"link-hover\""
		+ "\t\t\t\t\t\t\t          data-id=\""+ id +"\">"+ title +"</a>\n"
		+ "\t\t\t\t\t\t</h3>\n"
		+ "\t\t\t\t\t\t<table class=\"table table-borderless text-white mt-3\">\n"
		+ "\t\t\t\t\t\t\t<tbody class=\"todo-item-table\" id=\"todo-item-table_" + id + "\">\n"
		+ "\t\t\t\t\t\t\t</tbody>\n"
		+ "\t\t\t\t\t\t</table>\n"
		+ "\t\t\t\t\t</div>\n"
		+ "\t\t\t\t</div>\n"
		+ "\t\t\t</div> <!--Todo item-->"
	return template;
}