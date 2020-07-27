package com.todos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todos.dto.CreateTaskDTO;
import com.todos.dto.TodoDTO;
import com.todos.entity.Label;
import com.todos.entity.Task;
import com.todos.entity.Todo;
import com.todos.exception.RESTEntityNotFoundException;
import com.todos.repository.TodoRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
//...
@Transactional
@Service
public class JdbcTodoService implements iTodoService{
    @Qualifier("jdbcTaskService")
    private final iTaskService taskService;
    @Qualifier("jdbcLabelService")
    private final iLabelService labelService;
    private final TodoRepository todoRepository;
    private final ObjectMapper objectMapper;
    
    @Override
    public Todo save(TodoDTO todoDTO) {
        Todo todo = objectMapper.convertValue(todoDTO, Todo.class);
        Label label = labelService.find(todoDTO.getLabelId());
        todo.setLabel(label);
        
        todoRepository.save(todo);
        return todo;
    }
    
    @Override
    public Todo update(Long id, TodoDTO todoDTO) {
        Optional<Todo> opt = todoRepository.findById(id);
        opt.orElseThrow(() ->
            new RESTEntityNotFoundException(
                String.format("Todo with id : %s is not exists", id)));
        Todo todo = opt.get();
        todo.setTitle(todoDTO.getTitle());
        todoRepository.save(todo);
        return todo;
    }
    
    @Override
    public List<Todo> findAll() {
        Iterable<Todo> todos = todoRepository.findAll();
        todos.forEach(todo -> todo.getTasks().size());
        return iterableToList(todos);
    }
    
    @Override
    public List<Todo> findAllEager() {
        Iterable<Todo> todos = todoRepository.findAll();
        todos.forEach(todo -> {
            todo.getTasks().size();
//            Collections.sort(todo.getTasks(), Collections.reverseOrder()); // order desc
            todo.getTasks().sort(new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    return o2.getCreatedDate().compareTo(o1.getCreatedDate());
                }
            });
        });
    
        return iterableToList(todos);
    }
    
    private List<Todo> iterableToList(Iterable<Todo> todos) {
        return StreamSupport.stream(todos.spliterator(), false)
            .collect(Collectors.toList());
    }
    
    @Override
    public Todo find(Long id) {
        Optional<Todo> opt = todoRepository.findById(id);
        opt.orElseThrow(() ->
            new RESTEntityNotFoundException(
                String.format("Todo with id : %s is not exists", id)));
        Todo todo = opt.get();
        todo.getTasks().size();
        return todo;
    }
    
    @Override
    public void delete(Long id) {
        Todo todo = find(id);
        todoRepository.delete(todo);
    }
    
    @Override
    public Task addTask(Long id, CreateTaskDTO createTaskDTO) {
        Task task = taskService.save(createTaskDTO);
        Todo todo = find(id);
        todo.addTask(task);
        todoRepository.save(todo);
        return task;
    }
}
