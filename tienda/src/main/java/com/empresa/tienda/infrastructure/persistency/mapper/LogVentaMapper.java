package com.empresa.tienda.infrastructure.persistency.mapper;

import com.empresa.tienda.domain.model.LogVenta;
import com.empresa.tienda.infrastructure.persistency.entity.LogVentaEntity;
import org.springframework.stereotype.Component;

@Component
public class LogVentaMapper {

    public LogVenta toDomain(LogVentaEntity entity) {
        if (entity == null) return null;

        LogVenta venta = new LogVenta();
        venta.setId(entity.getId());
        venta.setPrendaId(entity.getPrendaId());
        venta.setPrecioVenta(entity.getPrecioVenta());
        venta.setFechaGeneracion(entity.getFechaGeneracion());

        return venta;
    }

    public LogVentaEntity toEntity(LogVenta domain) {
        if (domain == null) return null;

        LogVentaEntity entity = new LogVentaEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setPrendaId(domain.getPrendaId());
        entity.setPrecioVenta(domain.getPrecioVenta());
        entity.setFechaGeneracion(domain.getFechaGeneracion());

        return entity;
    }
}