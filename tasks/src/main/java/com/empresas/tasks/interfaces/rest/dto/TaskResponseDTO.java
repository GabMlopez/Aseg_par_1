package com.empresas.tasks.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String name;
    private String description;
    private String status;

    public TaskResponseDTO(Long id, String title, String name) {
        this.id = id;
        this.title =title;
        this.name = name;
    }
}