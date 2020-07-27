package com.todos.service;

import com.todos.dto.CreateTaskDTO;
import com.todos.dto.TaskDTO;
import com.todos.entity.Task;
import java.util.List;

public interface iTaskService {
    
    Task save(CreateTaskDTO createTaskDTO);
    
    Task update(Long id, TaskDTO taskDTO);
    
    List<Task> findAll();
    
    Task find(Long id);
    
    void delete(Long id);
}
