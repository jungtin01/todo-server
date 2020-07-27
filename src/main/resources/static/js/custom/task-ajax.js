// Links
var taskUri = MY_APP.contextPath + 'api/tasks';
var todoUri = MY_APP.contextPath + 'api/todos';

var taskModalInputId = "#task-modal-input";
var taskModalSubmitBtnId = "#task-modal-submit-btn";
var taskModalTaskContainerId = "#task-modal-task-container";
var todoItemTableClass = ".todo-item-table";
var todoItemTableContainerId = "#todo-item-table_";
var taskDelBtnId = ".task-delete-btn";
var isDoneCheckboxClass = ".task-checkbox-isdone";
var taskContentClass = ".task-content";

$(document).ready(function () {
	$(taskModalSubmitBtnId).click(function () {
		createTask(taskModalInputId);
	});

	$(taskModalTaskContainerId).on("click", taskDelBtnId, function () {
		deleteTask($(this));
	});

	$(todoItemTableClass).on("click", taskDelBtnId, function () {
		deleteTask($(this));
	});

	$(taskModalTaskContainerId).delegate(isDoneCheckboxClass, "change", function () {
		toggleIsDone($(this));
	});

	$(todoItemTableClass).delegate(isDoneCheckboxClass, "change", function () {
		toggleIsDone($(this));
	});


});

/*
*   CREATE
* */
function createTask(input) {
	resetTaskForm(input);

	var todoId = $(taskModalSubmitBtnId).attr("data-id");
	var data = {
		content: $(input).val()
	};

	$.ajax({
		url: todoUri + "/" + todoId + "/tasks",
		method: "post",
		data: JSON.stringify(data),
		contentType: "application/json; charset=utf-8",
		dataType: "json"
	}).done(function (data) {
		console.log(data);
		handleCreateTaskSuccess(input, data, todoId);
	}).fail(function (jqXHR, textStatus, errorThrown) {
		console.log(jqXHR);
		handleCreateTaskFail(input, jqXHR, textStatus, errorThrown);
	});
}

function handleCreateTaskSuccess(input, data, todoId) {
	// If successful
	var trTemplate = generateTaskTrTemplate(input, data);
	$(taskModalTaskContainerId).prepend(trTemplate);
	console.log(todoItemTableContainerId + data.id);
	$(todoItemTableContainerId + todoId).prepend(trTemplate);

	createToast("CREATE TASK",
		"success",
		"New todo has just been created successfully !");

	// reset
	$(taskModalInputId).val("");
}

function handleCreateTaskFail(input, jqXHR, textStatus, errorThrown) {
	// If fail
	var res = jqXHR.responseJSON;
	var errorsArr = res.errors;
	createToast("CREATE TASK ("+ res.error +")",
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
*   DELETE TASK
* */
function deleteTask(el) {
	var taskId = el.attr("data-id");

	$.ajax({
		url: taskUri + "/" + taskId,
		method: "delete",
		contentType: "application/json; charset=utf-8",
		dataType: "json"
	}).done(function (data) {
		console.log(data);
		handleDeleteTaskSuccess(data, taskId);
	}).fail(function (jqXHR, textStatus, errorThrown) {
		console.log(jqXHR);
		handleDeleteTaskFail(jqXHR, textStatus, errorThrown);
	});
}

function handleDeleteTaskSuccess(data, taskId) {
	// If successful
	var tr = $(taskDelBtnId+"[data-id=\""+ taskId +"\"]").closest("tr");
	tr.fadeOut(function() {
		tr.remove();
	});

	createToast("DELETE TASK",
		"success",
		"Task has just been deleted successfully !");
}

function handleDeleteTaskFail(jqXHR, textStatus, errorThrown) {
	// If fail
	var res = jqXHR.responseJSON;
	var errorsArr = res.errors;
	createToast("DELETE TASK ("+ res.error +")",
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
*   TOGGLE ISDONE
* */
function toggleIsDone(el) {
	var taskId = el.attr("data-id");
	var isDone = el.is(":checked");

	var data = {
		isDone: isDone
	};

	$.ajax({
		url: taskUri + "/" + taskId,
		method: "put",
		data: JSON.stringify(data),
		contentType: "application/json; charset=utf-8",
		dataType: "json"
	}).done(function (data) {
		console.log(data);
		handleToggleIsdoneSuccess(data, taskId);
	}).fail(function (jqXHR, textStatus, errorThrown) {
		console.log(jqXHR);
		handleToggleIsdoneFail(jqXHR, textStatus, errorThrown);
	});
}

function handleToggleIsdoneSuccess(data, taskId) {
	// If successful
	var content = $(taskContentClass + "[data-id=\""+ taskId +"\"]");
	var checkbox = $(isDoneCheckboxClass + "[data-id=\""+ taskId +"\"]");
	if(data.isDone) {
		content.addClass("task-done");
		checkbox.attr("checked", "checked")
	} else {
		content.removeClass("task-done");
		checkbox.removeAttr("checked");
	}
}

function handleToggleIsdoneFail(jqXHR, textStatus, errorThrown) {
	// If fail
	var res = jqXHR.responseJSON;
	createToast("DELETE TASK ("+ res.error +")",
		"danger",
		res.message);
}

/*
*   Addons
* */
function resetTaskForm(input) {
	// reset
	$(input).removeClass("is-invalid");
	$(".invalid-feedback").remove();
}

function generateTaskTrTemplate(input, data) {
	var taskId = data.id;
	var taskContent= data.content;
	var taskDoneClass = data.done ? "task-done" : "";
	var isSelected = data.done ? "checked" : "";

	var trTemplate =
		"<tr>\n"
		+ "\t\t\t\t\t\t\t<td><input type=\"checkbox\" "+ isSelected +" "
		+ "class=\"task-checkbox-isdone\" data-id=\""+ taskId +"\"></td>\n"
		+ "\t\t\t\t\t\t\t<td class=\"task-content "+ taskDoneClass +"\" data-id=\""+ taskId +"\">\n"
		+ taskContent
		+ "\t\t\t\t\t\t\t</td>\n"
		+ "\t\t\t\t\t\t\t<td>\n"
		+ "\t\t\t\t\t\t\t\t<button class=\"btn btn-sm btn-outline-danger "
		+ "rounded-0 task-delete-btn\" data-id=\""+ taskId +"\">Remove</button>\n"
		+ "\t\t\t\t\t\t\t</td>\n"
		+ "\t\t\t\t\t\t</tr>";
	return trTemplate;
}