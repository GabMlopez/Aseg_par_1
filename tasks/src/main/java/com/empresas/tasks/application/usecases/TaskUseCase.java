package com.empresas.tasks.application.usecases;

import com.empresas.tasks.application.ports.out.TaskRepositoryPort;
import com.empresas.tasks.domain.model.TaskStatus;
import com.empresas.tasks.domain.model.Task;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskUseCase {
    private final TaskRepositoryPort taskRepository;


    @Transactional
    public Task createTask(Task task){
        task.setStatus(TaskStatus.PENDING);
        return taskRepository.save(task);
    }
}
