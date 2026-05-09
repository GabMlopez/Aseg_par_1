package com.empresas.tasks.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskCreateDTO {
    @NotBlank(message = "Titulo Obligatorio")
    private String title;
    private String description;
}
