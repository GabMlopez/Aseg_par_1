package com.empresas.tasks.application.ports.out;

import com.empresas.tasks.domain.model.Task;
import java.util.Optional;

public interface TaskRepositoryPort {
    Task save(Task task);
    Optional<Task> findById(Long id);
}
