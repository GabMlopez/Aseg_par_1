package com.empresa.tienda.infrastructure.persistency.repository;

import com.empresa.tienda.infrastructure.persistency.entity.LogAccesoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaLogAccesoRepository extends JpaRepository<LogAccesoEntity, Long> {

    List<LogAccesoEntity> findByUsuarioId(Long usuarioId);

    List<LogAccesoEntity> findByFechaBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT l FROM LogAccesoEntity l WHERE DATE(l.fecha) = DATE(:fecha)")
    List<LogAccesoEntity> findByDateOnly(@Param("fecha") LocalDateTime fecha);

    List<LogAccesoEntity> findByUsuarioIdAndFechaBetween(Long usuarioId, LocalDateTime start, LocalDateTime end);
}