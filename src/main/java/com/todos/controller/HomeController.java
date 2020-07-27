package com.todos.controller;

import com.todos.entity.Label;
import com.todos.entity.Todo;
import com.todos.service.iLabelService;
import com.todos.service.iTodoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
//...
@Controller
public class HomeController {
    @Qualifier("jdbcLabelService")
    private final iLabelService labelService;
    
    @Qualifier("jdbcTodoService")
    private final iTodoService todoService;
    
    @ModelAttribute("labels")
    public List<Label> getLabels() {
        return labelService.findAll();
    }
    
    @ModelAttribute("todos")
    public List<Todo> getTodos() {
        return todoService.findAllEager();
    }
    
    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }
}
