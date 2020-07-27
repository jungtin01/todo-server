package com.todos.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateTaskDTO {
    @NotBlank
    @NotNull
    private String content;
    
//    @NotNull
    private Boolean isDone = false;
}
