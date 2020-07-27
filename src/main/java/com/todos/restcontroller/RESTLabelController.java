package com.todos.restcontroller;

import com.todos.dto.LabelDTO;
import com.todos.entity.Label;
import com.todos.service.iLabelService;
import java.net.URI;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
//...
@RequestMapping(value = "/api/labels")
@RestController
public class RESTLabelController {
    
    @Qualifier("jdbcLabelService")
    private final iLabelService labelService;
    
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(produces = {"application/hal+json", "application/json"})
    public CollectionModel<Label> getAllLabels() {
        List<Label> labels = labelService.findAll();
        labels.stream().forEach(label -> label.setTodos(null));
        
        return CollectionModel.of(labels, getSelfRelLink());
    }
    
    @PostMapping
    public EntityModel<Object> save(@Valid @RequestBody LabelDTO labelDTO) {
        Label label = labelService.save(labelDTO);
        Link link = getSelfRelLinkLabel(label.getId());
        return EntityModel.of(label, link);
    }
    
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/{id}")
    public EntityModel<Label> getLabel(@PathVariable Long id) {
        Label label = labelService.find(id);
        label.getTodos().stream().forEach(todo -> {
            Link self = RESTTodoController.getSelfRelLinkTodo(todo.getId()).withSelfRel();
            todo.add(self);
        });
        Link getAllLink = getSelfRelLink();
        return EntityModel.of(label, getAllLink);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id,
        @Valid @RequestBody LabelDTO labelDTO) {
        Label label = labelService.update(id, labelDTO);
        URI uri = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(RESTLabelController.class).getLabel(label.getId()))
            .toUri();
        return ResponseEntity.created(uri).build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        labelService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    /*
    *
    * */
    public static Link getSelfRelLinkLabel(Long id) {
        return WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(
                RESTLabelController.class).getLabel(id)).withSelfRel();
    }
    
    public static Link getSelfRelLink() {
        return WebMvcLinkBuilder.linkTo(
            RESTTodoController.class).withSelfRel();
    }
    
}
