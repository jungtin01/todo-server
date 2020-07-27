package com.todos.restcontroller;

import com.todos.dto.CreateTaskDTO;
import com.todos.dto.TodoDTO;
import com.todos.entity.Task;
import com.todos.entity.Todo;
import com.todos.service.iTodoService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
//...
@RequestMapping(value = "/api/todos")
@RestController
public class RESTTodoController {
    
    @Qualifier("jdbcTodoService")
    private final iTodoService todoService;
    
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(produces = {"application/hal+json", "application/json"})
    public CollectionModel<Todo> getAllTodos(@RequestParam(value = "withtask",
        required = false,  defaultValue = "false") boolean isWithTask) {
        List<Todo> todos;
        if(isWithTask) {
            todos = todoService.findAllEager();
            // no link need for task
        } else {
            todos = todoService.findAll();
            todos.stream().forEach(todo -> todo.setTasks(null));
        }
        todos.stream().forEach(todo -> {
            todo.add(getSelfRelLinkTodo(todo.getId()));
        });
        return CollectionModel.of(todos, getSelfRelLink());
    }
    
    @PostMapping
    public EntityModel<Object> save(@Valid @RequestBody TodoDTO todoDTO) {
        Todo todo = todoService.save(todoDTO);
        todo.setTasks(null);
        
        Link getAllLink = getSelfRelLink();
        return EntityModel.of(todo, getAllLink);
    }
    
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public EntityModel<Object> getTodo(@PathVariable Long id) {
        Todo todo = todoService.find(id);
        todo.add(getSelfRelLinkTodo(todo.getId()));
        return EntityModel.of(todo);
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{id}")
    public EntityModel<Object> update(@PathVariable Long id,
        @Valid @RequestBody TodoDTO todoDTO) {
        Todo todo = todoService.update(id, todoDTO);
        todo.add(getSelfRelLinkTodo(todo.getId()));
        return EntityModel.of(todo);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    /*
    *   Tasks
    * */
    @PostMapping("/{id}/tasks")
    public EntityModel<Object> addTask(@PathVariable Long id,
        @RequestBody CreateTaskDTO createTaskDTO) {
        Task task = todoService.addTask(id, createTaskDTO);
        
        return EntityModel.of(task);
    }
    
    public static Link getSelfRelLinkTodo(Long id) {
        return WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(
                RESTTodoController.class).getTodo(id)).withSelfRel();
    }
    
    public static Link getSelfRelLink() {
        return WebMvcLinkBuilder.linkTo(
                RESTTodoController.class).withSelfRel();
    }
}