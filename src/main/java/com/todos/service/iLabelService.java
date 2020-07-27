package com.todos.service;

import com.todos.dto.LabelDTO;
import com.todos.entity.Label;
import java.util.List;

public interface iLabelService {
    
    Label save(LabelDTO todoDTO);
    
    Label update(Long id, LabelDTO labelDTO);
    
    List<Label> findAll();
    
    Label find(Long id);
    
    void delete(Long id);
}
