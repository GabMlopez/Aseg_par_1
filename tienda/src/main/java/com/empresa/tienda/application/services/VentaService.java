package com.empresa.tienda.application.services;

import com.empresa.tienda.domain.model.LogVenta;
import com.empresa.tienda.domain.model.Prenda;
import com.empresa.tienda.domain.ports.LogVentaRepositoryPort;
import com.empresa.tienda.domain.ports.PrendaRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VentaService {
    private final PrendaRepositoryPort prendaRepository;
    private final LogVentaRepositoryPort logVentaRepository;

    @Transactional
    public LogVenta realizarVenta (Long prendaId, Integer cantidad){
        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada"));

        if (prenda.getCantidad() < cantidad){
            throw new RuntimeException("Stock INsuficiente");
        }

        Integer nuevaCantidad = prenda.getCantidad() -cantidad;
        prendaRepository.updateStock(prendaId,nuevaCantidad);

        LogVenta venta = new LogVenta();
        venta.setPrendaId(prendaId);
        venta.setPrecioVenta(prenda.getPrecio()*cantidad);
        venta.setFechaGeneracion(LocalDateTime.now());

        return logVentaRepository.save(venta);
    }
}
