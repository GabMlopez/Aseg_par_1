package com.empresa.tienda.infrastructure.persistency.repository;

import com.empresa.tienda.infrastructure.persistency.entity.LogVentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaLogVentaRepository extends JpaRepository<LogVentaEntity, Long> {

    List<LogVentaEntity> findByFechaGeneracion(LocalDateTime fecha);

    List<LogVentaEntity> findByFechaGeneracionBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT l FROM LogVentaEntity l WHERE DATE(l.fechaGeneracion) = DATE(:fecha)")
    List<LogVentaEntity> findByDateOnly(@Param("fecha") LocalDateTime fecha);
}