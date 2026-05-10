package com.empresa.tienda.infrastructure.persistency.adapter;

import com.empresa.tienda.domain.model.Prenda;
import com.empresa.tienda.domain.ports.PrendaRepositoryPort;
import com.empresa.tienda.infrastructure.persistency.entity.PrendaEntity;
import com.empresa.tienda.infrastructure.persistency.mapper.PrendaMapper;
import com.empresa.tienda.infrastructure.persistency.repository.JpaPrendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PrendaRepositoryAdapter implements PrendaRepositoryPort {

    private final JpaPrendaRepository jpaRepository;
    private final PrendaMapper mapper;

    @Override
    public List<Prenda> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Prenda> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Prenda save(Prenda prenda) {
        PrendaEntity entity = mapper.toEntity(prenda);
        PrendaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Prenda updateStock(Long id, Integer nuevaCantidad) {
        jpaRepository.updateStock(id, nuevaCantidad);
        return findById(id).orElseThrow(() -> new RuntimeException("Prenda no encontrada"));
    }
}