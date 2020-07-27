package com.todos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todos.dto.CreateTaskDTO;
import com.todos.dto.TaskDTO;
import com.todos.entity.Task;
import com.todos.exception.RESTEntityNotFoundException;
import com.todos.repository.TaskRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
//...
@Transactional
@Service
public class JdbcTaskService implements iTaskService {
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;
    @Override
    public Task save(CreateTaskDTO taskDTO) {
        Task task = objectMapper.convertValue(taskDTO, Task.class);
        taskRepository.save(task);
        return task;
    }
    
    @Override
    public Task update(Long id, TaskDTO taskDTO) {
        Optional<Task> opt = taskRepository.findById(id);
        opt.orElseThrow(() ->
            new RESTEntityNotFoundException(
                String.format("Task with id : %s is not exists", id)));
        Task task = opt.get();
        if(taskDTO.getContent() != null)
            task.setContent(taskDTO.getContent());
        
        if(taskDTO.getIsDone() != null)
            task.setIsDone(taskDTO.getIsDone());
        else
            task.setIsDone(false); // default
        taskRepository.save(task);
        return task;
    }
    
    @Override
    public List<Task> findAll() {
        Iterable<Task> tasks = taskRepository.findAll();
        return iterableToList(tasks);
    }
    
    private List<Task> iterableToList(Iterable<Task> tasks) {
        return StreamSupport.stream(tasks.spliterator(), false)
            .collect(Collectors.toList());
    }
    
    @Override
    public Task find(Long id) {
        Optional<Task> opt = taskRepository.findById(id);
        opt.orElseThrow(() ->
            new RESTEntityNotFoundException(
                String.format("Task with id : %s is not exists", id)));
        Task task = opt.get();
        return task;
    }
    
    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
