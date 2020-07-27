package com.todos.data;

import com.todos.entity.Label;
import com.todos.entity.Task;
import com.todos.entity.Todo;
import com.todos.repository.LabelRepository;
import com.todos.repository.TodoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
//...
@Transactional
@Component
public class DataTodoInit implements ApplicationRunner {
    private final TodoRepository todoRepository;
    private final LabelRepository labelRepository;
    
    public void run(ApplicationArguments args) throws Exception {
        List<Label> labels = List.of(
            new Label("Quan trọng nhất"),
            new Label("Cần hoàn tất trong ngày"),
            new Label("Chưa cần làm lắm"),
            new Label("Note list")
        );
        labelRepository.saveAll(labels);
    
        //...
        List<Task> tasks = List.of(
            new Task("Thêm trang login vào", false),
            new Task("Chỉnh sửa trang register", false),
            new Task("Hiển thị link login & register", false),
            new Task("Register --> Đăng nhập luôn", false),
            new Task("Fix authority nav error", false)
        );
        Todo todo = new Todo("Tạo trang login");
        for (Task task : tasks) {
            todo.addTask(task);
        }
        todo.setLabel(labelRepository.findById(1L).get());
        todoRepository.save(todo);
        
        //...
        tasks = List.of(
            new Task("Chuyển hết Setting methods từ UserController --> AuthController", false),
            new Task("Chỉnh lại thằng URI trong GetLinkREST", false),
            new Task("Update Thumbnail (String --> Class)", false)
        );
        todo = new Todo("Chỉnh lại thằng Profile cho ROLE_USER");
        for (Task task : tasks) {
            todo.addTask(task);
        }
        todo.setLabel(labelRepository.findById(1L).get());
        todoRepository.save(todo);
        
        //...
        tasks = List.of(
            new Task("Thêm ~ thông tin phụ về User", false),
            new Task("Chỉnh sửa phần view về User", false),
            new Task("View trang theo User", false)
        );
        todo = new Todo("Chỉnh sửa User");
        for (Task task : tasks) {
            todo.addTask(task);
        }
        todo.setLabel(labelRepository.findById(2L).get());
        todoRepository.save(todo);
    }
}
