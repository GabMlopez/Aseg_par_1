package com.empresa.tienda.domain.ports;

import com.empresa.tienda.domain.model.Prenda;

import java.util.List;
import java.util.Optional;

public interface PrendaRepositoryPort {
    List<Prenda> findAll();
    Optional<Prenda> findById(Long id);
    Prenda save(Prenda prenda);
    void deleteById(Long id);
    Prenda updateStock(Long id, Integer nuevaCantidad);
}
