package com.todos.entity;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@Data
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"})
//...
@Entity
public class Todo extends RepresentationModel<Todo> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @OneToMany(mappedBy = "todo",
        cascade = {PERSIST, REMOVE},
        fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();
    
    @JsonIgnore
    @ManyToOne
    private Label label;
    
    @UpdateTimestamp
    private LocalDateTime updatedDate;
    
    @CreationTimestamp
    private LocalDateTime createdDate;
    
    public Todo(String title) {
        this.title = title;
    }
    
    //...
    @PreRemove
    protected void preRemove() {
        //...
    }
    
    public void addTask(Task task) {
        task.setTodo(this);
        this.tasks.add(task);
    }
    
    public void removeTask(Task task) {
        task.setTodo(null);
        this.tasks.remove(task);
    }
}
