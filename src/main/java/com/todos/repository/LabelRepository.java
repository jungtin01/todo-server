package com.todos.repository;

import com.todos.entity.Label;
import org.springframework.data.repository.CrudRepository;

public interface LabelRepository extends CrudRepository<Label, Long> {
    
}
