package com.empresas.tasks.domain.model;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

        private  Long id;
        private String title;
        private  String description;
        private TaskStatus status;
        private LocalDateTime createdAt;

        public void markAsCompleted(){
            if (this.status == TaskStatus.CANCELLED){
                throw new IllegalStateException("Cannot complete cancelled task");
            }
            this.status = TaskStatus.COMPLETED;
        }
}
