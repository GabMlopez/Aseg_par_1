package com.empresas.tasks.interfaces.rest.mapper;

import com.empresas.tasks.domain.model.Task;
import com.empresas.tasks.interfaces.rest.dto.TaskCreateDTO;
import com.empresas.tasks.interfaces.rest.dto.TaskResponseDTO;

public class TaskMapper {
    public static Task toDomain(TaskCreateDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        return task;
    }

    public static TaskResponseDTO toDto(Task task) {
        // Implementar conversión a TaskResponseDTO
        return new TaskResponseDTO(task.getId(), task.getTitle(), task.getStatus().name());
    }
}