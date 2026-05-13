package com.empresa.tienda;
/*
import com.empresa.tienda.application.services.VentaService;
import com.empresa.tienda.domain.model.LogVenta;
import com.empresa.tienda.domain.model.Pantalon;
import com.empresa.tienda.domain.ports.LogVentaRepositoryPort;
import com.empresa.tienda.domain.ports.PrendaRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {
    @Mock
    private PrendaRepositoryPort prendaRepository;

    @Mock
    private LogVentaRepositoryPort logVentaRepository;

    @InjectMocks
    private VentaService ventaService;

    @Test
    void shouldCreateSaleSuccessfully_CorrectnessValidated() {
        // Arrange
        Long prendaId = 28L;
        int cantidadComprada = 2;
        int stockInicial = 10;
        int stockEsperado = stockInicial - cantidadComprada; // 8

        Pantalon pantalon = new Pantalon(prendaId, "Nike", "M", 50.0, stockInicial);

        // Mock de findById
        when(prendaRepository.findById(prendaId)).thenReturn(Optional.of(pantalon));

        // Mock de updateStock
        when(prendaRepository.updateStock(prendaId, stockEsperado)).thenReturn(pantalon);

        LogVenta ventaEsperada = new LogVenta();
        ventaEsperada.setPrendaId(prendaId);
        ventaEsperada.setPrecioVenta(100.0);
        ventaEsperada.setFechaGeneracion(LocalDateTime.now());

        when(logVentaRepository.save(any(LogVenta.class))).thenReturn(ventaEsperada);

        // Act
        LogVenta venta = ventaService.realizarVenta(prendaId, cantidadComprada);

        // Assert
        assertNotNull(venta);
        assertEquals(prendaId, venta.getPrendaId());
        assertEquals(100.0, venta.getPrecioVenta());

        verify(prendaRepository, times(1)).findById(prendaId);
        verify(prendaRepository, times(1)).updateStock(prendaId, stockEsperado);
        verify(logVentaRepository, times(1)).save(any(LogVenta.class));
    }

    @Test
    void shouldThrowExceptionWhenInsufficientStock_IntegrityValidated() {
        Long prendaId = 1L;
        int cantidadComprada = 5;
        int stockInicial = 1;

        Pantalon pantalon = new Pantalon(prendaId, "Nike", "M", 50.0, stockInicial);
        when(prendaRepository.findById(prendaId)).thenReturn(Optional.of(pantalon));

        assertThrows(RuntimeException.class, () -> ventaService.realizarVenta(prendaId, cantidadComprada));

        verify(prendaRepository, never()).updateStock(any(), any());
        verify(logVentaRepository, never()).save(any());
    }
}

 */