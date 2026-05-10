package com.empresa.tienda.domain.ports;


import com.empresa.tienda.domain.model.LogVenta;

import java.time.LocalDateTime;
import java.util.List;

public interface LogVentaRepositoryPort {
    LogVenta save(LogVenta venta);
    List<LogVenta> findAll();
    List<LogVenta> findByDate(LocalDateTime date);
}
