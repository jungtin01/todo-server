package com.todos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TaskDTO {
    private Long id;
    
//    @NotBlank
//    @NotNull
    private String content;
    
//    @NotNull
    private Boolean isDone;
}
