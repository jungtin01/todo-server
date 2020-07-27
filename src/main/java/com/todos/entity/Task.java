package com.todos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, columnDefinition = "text")
    private String content;
    
    @Column(nullable = false)
    private Boolean isDone = false;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Todo todo;
    
    @UpdateTimestamp
    private LocalDateTime updatedDate;
    
    @CreationTimestamp
    private LocalDateTime createdDate;
    
    public Task(String content, boolean isDone) {
        this.content = content;
        this.isDone = isDone;
    }
}
