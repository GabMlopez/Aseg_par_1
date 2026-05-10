package com.empresa.tienda.infrastructure.persistency.mapper;

import com.empresa.tienda.domain.model.LogAcceso;
import com.empresa.tienda.infrastructure.persistency.entity.LogAccesoEntity;
import org.springframework.stereotype.Component;

@Component
public class LogAccesoMapper {

    public LogAcceso toDomain(LogAccesoEntity entity) {
        if (entity == null) return null;

        LogAcceso log = new LogAcceso();
        log.setId(entity.getId());
        log.setUsuarioId(entity.getUsuarioId());
        log.setActividad(entity.getActividad());
        log.setFecha(entity.getFecha());

        return log;
    }

    public LogAccesoEntity toEntity(LogAcceso domain) {
        if (domain == null) return null;

        LogAccesoEntity entity = new LogAccesoEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setUsuarioId(domain.getUsuarioId());
        entity.setActividad(domain.getActividad());
        entity.setFecha(domain.getFecha());

        return entity;
    }
}