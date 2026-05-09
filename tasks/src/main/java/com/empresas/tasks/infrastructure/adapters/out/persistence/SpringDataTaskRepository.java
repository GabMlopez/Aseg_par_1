package com.empresas.tasks.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataTaskRepository extends JpaRepository<TaskEntity, Long> {
    // Spring Data JPA generará automáticamente la implementación de
    // métodos como save(), findById(), delete(), etc.
}