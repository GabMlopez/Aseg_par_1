package com.empresas.tasks.interfaces.rest;

import com.empresas.tasks.application.usecases.TaskUseCase;
import com.empresas.tasks.domain.model.Task;
import com.empresas.tasks.interfaces.rest.dto.TaskCreateDTO;
import com.empresas.tasks.interfaces.rest.dto.TaskResponseDTO;
import com.empresas.tasks.interfaces.rest.mapper.TaskMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskUseCase taskUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDTO create(@Valid @RequestBody TaskCreateDTO dto){
        Task task = TaskMapper.toDomain(dto);
        Task createdTask = taskUseCase.createTask(task);
        return TaskMapper.toDto(createdTask);
    }

}
