package com.empresas.tasks;

import com.empresas.tasks.application.ports.out.TaskRepositoryPort;
import com.empresas.tasks.application.usecases.TaskUseCase;
import com.empresas.tasks.domain.model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskUseCaseTest {
    @Mock
    private TaskRepositoryPort taskRepository;

    @InjectMocks
    private TaskUseCase taskUseCase;

    @Test
    void shouldCreateTaskSuccessfully_CorrectnessValidated(){
        Task task = new Task(null, "Test", "Desc", null, null);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskUseCase.createTask(task);

        assertNotNull(result);
        verify(taskRepository,times(1)).save(task);
    }

}
