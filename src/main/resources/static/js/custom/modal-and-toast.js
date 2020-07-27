$(document).ready(function () {
	var todosContainerClass = ".todos-container";
	var triggerTaskModalClass = ".trigger-task-modal";
	$(todosContainerClass).on("click", triggerTaskModalClass, function() {
		triggerTaskModal($(this));
	});

	var triggerTodoModalClass = ".trigger-todo-modal";
	$(triggerTodoModalClass).click(function() {
		triggerTodoModal($(this));
	});

});

/*
*   TODO MODAL
* */
function triggerTodoModal(el) {
	$('#todo-modal').modal('show');
}

/*
*   TASK MODAL
* */
function triggerTaskModal(el) {
	var todoUri = MY_APP.contextPath + 'api/todos';
	var todoId = el.attr("data-id");

	var taskModalInputId = "#task-modal-input";
	var taskModalSubmitBtnId = "#task-modal-submit-btn";
	var taskModalIdId = "#task-modal-id";
	var taskModalTitleId = "#task-modal-title";
	var taskModalTaskContainerId = "#task-modal-task-container";
	var taskModalTaskContainer = "task-modal-task-container_";
	var todoItemTableContainer = "todo-item-table_";
	var taskDelBtnId = ".task-delete-btn";


	$.ajax({ /* TRIGGER TASK MODAL */
		url: todoUri + "/" + todoId + "?withtask=true",
		method: "get",
		contentType: "application/json; charset=utf-8",
		dataType: "json"
	}).done(function (data) {
		console.log(data);
		//setting up
		var id = data.id;
		var title = data.title;
		$(taskModalIdId).text(id);
		$(taskModalTitleId).text(title);

		var template = generateTaskModalTaskTemplate(data.tasks);
		$(taskModalTaskContainerId).html(template);
		$(taskModalTaskContainerId).addClass(taskModalTaskContainer + id);

		$(taskModalSubmitBtnId).attr("data-id", id); // set objId

		$('#task-modal').modal('show');
	}).fail(function (jqXHR, textStatus, errorThrown) {
		console.log(jqXHR);
		createToast("Task Modal Error",
			"danger",
			textStatus + " : " + errorThrown);
	});
}

function generateTaskModalTaskTemplate(tasks) {
	var template = "";
	$.each(tasks, function(index, value) {
		var taskId = value.id;
		var taskContent= value.content;
		var taskDoneClass = value.isDone ? "task-done" : "";
		var isSelected = value.isDone ? "checked" : "";

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

		// Xem xét lại thằng Checked

		template += trTemplate;
	});
	return template;
}

/* TOAST */
function createToast(heading, cssClass, content) {
	var randomId = Math.random().toString(36).substring(7);
	var delayTime = 5000; // millis
	var template = createToastTemplate(heading,
		cssClass,
		content,
		randomId, delayTime);

	$("#toast-container").append(template);
	$("#" + randomId).toast('show');
	setTimeout(function () {
		$("#" + randomId).remove();
	}, delayTime + 300);

}

function createToastTemplate(heading, cssClass, content, id, delayTime) {
	var autohide = true;
	var template =
		"<div class=\"toast\"" +
		"id=\"" + id + "\"" +
		"role=\"alert\" aria-live=\"assertive\" aria-atomic=\"true\"" +
		"data-delay=\"" + delayTime + "\"" +
		"data-autohide=\"" + autohide + "\"" +
		">" +
		"<div class=\"toast-header bg-" + cssClass + " text-white\">" +
		"<strong class=\"mr-auto\">" + heading + "</strong>" +
		"<small>just now</small>" +
		"<button type=\"button\" class=\"ml-2 mb-1 close\" data-dismiss=\"toast\" aria-label=\"Close\">"
		+
		"<span aria-hidden=\"true\">&times;</span>" +
		"</button>" +
		"</div>" +
		"<div class=\"toast-body\">" +
		content
	"</div>" +
	"</div>";
	return template;
}