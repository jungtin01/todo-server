package com.todos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todos.dto.LabelDTO;
import com.todos.entity.Label;
import com.todos.exception.RESTEntityNotFoundException;
import com.todos.repository.LabelRepository;
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
public class JdbcLabelService implements iLabelService {
    private final LabelRepository labelRepository;
    private final ObjectMapper objectMapper;
    @Override
    public Label save(LabelDTO labelDTO) {
        Label label = objectMapper.convertValue(labelDTO, Label.class);
        labelRepository.save(label);
        return label;
    }
    
    @Override
    public Label update(Long id, LabelDTO labelDTO) {
        Optional<Label> opt = labelRepository.findById(id);
        opt.orElseThrow(() ->
            new RESTEntityNotFoundException(
                String.format("Label with id : %s is not exists", id)));
        Label label = opt.get();
        label.setTitle(labelDTO.getTitle());
        labelRepository.save(label);
        return label;
    }
    
    @Override
    public List<Label> findAll() {
        Iterable<Label> labels = labelRepository.findAll();
        return iterableToList(labels);
    }
    
    private List<Label> iterableToList(Iterable<Label> labels) {
        return StreamSupport.stream(labels.spliterator(), false)
            .collect(Collectors.toList());
    }
    
    @Override
    public Label find(Long id) {
        Optional<Label> opt = labelRepository.findById(id);
        opt.orElseThrow(() ->
            new RESTEntityNotFoundException(
                String.format("Label with id : %s is not exists", id)));
        Label label = opt.get();
        
        label.getTodos().forEach(todo -> {
            todo.getTasks().size();
        });
        return label;
    }
    
    @Override
    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}
