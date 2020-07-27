package com.todos.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@NoArgsConstructor
@Data
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"})
//...
@Entity
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @OneToMany(mappedBy = "label")
    private List<Todo> todos = new ArrayList<>();
    
    @UpdateTimestamp
    private LocalDateTime updatedDate;
    
    @CreationTimestamp
    private LocalDateTime createdDate;
    
    public Label(String title) {
        this.title = title;
    }
    
    //...
    @PreRemove
    protected void preRemove() {
        todos.parallelStream().forEach(todo -> {
            todo.setLabel(null);
        });
    }
    
    public void addTodo(Todo todo) {
        todo.setLabel(this);
        this.todos.add(todo);
    }
    
    public void removeTodo(Todo todo) {
        todo.setLabel(null);
        this.todos.remove(todo);
    }
}
