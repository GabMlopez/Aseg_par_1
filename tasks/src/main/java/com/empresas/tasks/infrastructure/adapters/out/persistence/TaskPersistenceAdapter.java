package com.empresas.tasks.infrastructure.adapters.out.persistence;

import com.empresas.tasks.application.ports.out.TaskRepositoryPort;
import com.empresas.tasks.domain.model.Task;
import com.empresas.tasks.domain.model.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements TaskRepositoryPort {

    private final SpringDataTaskRepository repository;

    @Override
    public Task save(Task task) {
        // 1. Mapeo de Dominio a Entidad
        TaskEntity entity = new TaskEntity();
        entity.setId(task.getId());
        entity.setTitle(task.getTitle());
        entity.setDescription(task.getDescription());
        entity.setStatus(task.getStatus());

        // 2. Guardar con Spring Data
        TaskEntity savedEntity = repository.save(entity);

        // 3. Actualizar el dominio con el ID generado
        task.setId(savedEntity.getId());
        return task;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    private Task toDomain(TaskEntity entity) {
        Task task = new Task();
        task.setId(entity.getId());
        task.setTitle(entity.getTitle());
        task.setDescription(entity.getDescription());
        task.setStatus(entity.getStatus());
        return task;
    }
}