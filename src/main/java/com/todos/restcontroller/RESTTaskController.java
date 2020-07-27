package com.todos.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todos.dto.TaskDTO;
import com.todos.entity.Task;
import com.todos.service.JdbcTaskService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
//...
@RequestMapping(value = "/api/tasks")
@RestController
public class RESTTaskController {
    
    private final ObjectMapper objectMapper;
    private final JdbcTaskService jdbcTaskService;
    
    @GetMapping("/{id}")
    public EntityModel<Object> getTask(@PathVariable Long id) {
        Task task = jdbcTaskService.find(id);
        Link selfLink = getSelfRelLinkTask(task.getId());
        return EntityModel.of(task, selfLink);
    }
    
    @PutMapping("/{id}")
    public EntityModel<Object> update(@PathVariable Long id,
        @Valid @RequestBody TaskDTO taskDTO) {
        Task task = jdbcTaskService.update(id, taskDTO);
        return EntityModel.of(objectMapper.convertValue(task, TaskDTO.class));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        jdbcTaskService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    /*
    *
    * */
    public static Link getSelfRelLinkTask(Long id) {
        return WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(
                RESTTaskController.class).getTask(id)).withSelfRel();
    }
    
}
