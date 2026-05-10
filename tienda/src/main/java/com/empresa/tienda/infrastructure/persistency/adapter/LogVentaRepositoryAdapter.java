package com.empresa.tienda.infrastructure.persistency.adapter;

import com.empresa.tienda.domain.model.LogVenta;
import com.empresa.tienda.domain.ports.LogVentaRepositoryPort;
import com.empresa.tienda.infrastructure.persistency.entity.LogVentaEntity;
import com.empresa.tienda.infrastructure.persistency.mapper.LogVentaMapper;
import com.empresa.tienda.infrastructure.persistency.repository.JpaLogVentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LogVentaRepositoryAdapter implements LogVentaRepositoryPort {

    private final JpaLogVentaRepository jpaRepository;
    private final LogVentaMapper mapper;

    @Override
    public LogVenta save(LogVenta venta) {
        LogVentaEntity entity = mapper.toEntity(venta);
        LogVentaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<LogVenta> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<LogVenta> findByDate(LocalDateTime fecha) {
        LocalDateTime startOfDay = fecha.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = fecha.toLocalDate().atTime(23, 59, 59);

        return jpaRepository.findByFechaGeneracionBetween(startOfDay, endOfDay).stream()
                .map(mapper::toDomain)
                .toList();
    }
}