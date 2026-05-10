package com.empresa.tienda.infrastructure.persistency.adapter;

import com.empresa.tienda.domain.model.LogAcceso;
import com.empresa.tienda.domain.ports.LogAccesoRepositoryPort;
import com.empresa.tienda.infrastructure.persistency.entity.LogAccesoEntity;
import com.empresa.tienda.infrastructure.persistency.mapper.LogAccesoMapper;
import com.empresa.tienda.infrastructure.persistency.repository.JpaLogAccesoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LogAccesoRepositoryAdapter implements LogAccesoRepositoryPort {

    private final JpaLogAccesoRepository jpaRepository;
    private final LogAccesoMapper mapper;

    @Override
    public LogAcceso save(LogAcceso log) {
        LogAccesoEntity entity = mapper.toEntity(log);
        LogAccesoEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<LogAcceso> findByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<LogAcceso> findByDate(LocalDateTime fecha) {
        // Buscar por día completo (desde las 00:00:00 hasta las 23:59:59)
        LocalDateTime startOfDay = fecha.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = fecha.toLocalDate().atTime(23, 59, 59);

        return jpaRepository.findByFechaBetween(startOfDay, endOfDay).stream()
                .map(mapper::toDomain)
                .toList();
    }
}