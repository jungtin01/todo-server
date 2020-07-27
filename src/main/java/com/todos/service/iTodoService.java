package com.todos.service;

import com.todos.dto.CreateTaskDTO;
import com.todos.dto.TodoDTO;
import com.todos.entity.Task;
import com.todos.entity.Todo;
import java.util.List;

public interface iTodoService {
    
    Todo save(TodoDTO todoDTO);
    
    Todo update(Long id, TodoDTO todoDTO);
    
    List<Todo> findAll();
    
    List<Todo> findAllEager();
    
    Todo find(Long id);
    
    void delete(Long id);
    
    Task addTask(Long id, CreateTaskDTO taskDTO);
}
