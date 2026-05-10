package com.empresa.tienda.domain.ports;

import com.empresa.tienda.domain.model.LogAcceso;

import java.time.LocalDateTime;
import java.util.List;

public interface LogAccesoRepositoryPort {
    LogAcceso save(LogAcceso acceso);
    List<LogAcceso> findByUsuarioId(Long usuario_id);
    List<LogAcceso> findByDate(LocalDateTime date);
}
