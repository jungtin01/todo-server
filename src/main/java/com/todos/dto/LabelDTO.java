package com.todos.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LabelDTO {
    @NotBlank
    @NotNull
    private String title;
}
