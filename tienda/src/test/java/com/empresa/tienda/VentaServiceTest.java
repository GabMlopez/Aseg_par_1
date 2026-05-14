package com.empresa.tienda;

import com.empresa.tienda.application.services.VentaService;
import com.empresa.tienda.domain.model.Camisa;
import com.empresa.tienda.domain.model.LogVenta;
import com.empresa.tienda.domain.model.Pantalon;
import com.empresa.tienda.domain.ports.LogVentaRepositoryPort;
import com.empresa.tienda.domain.ports.PrendaRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VentaService - Pruebas Unitarias")
class VentaServiceTest {

    @Mock
    private PrendaRepositoryPort prendaRepository;

    @Mock
    private LogVentaRepositoryPort ventaRepository;

    @InjectMocks
    private VentaService ventaService;

    // -------------------------------------------------------------------------
    // TC-VS-01: Venta exitosa con Pantalón
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-VS-01: Venta exitosa – descuenta stock y registra LogVenta")
    void shouldCreateSaleSuccessfully_CorrectnessValidated() {
        // Arrange
        Pantalon pantalon = new Pantalon(1L, "Nike", "M", 50.0, 10);
        LogVenta logEsperado = new LogVenta();
        logEsperado.setPrendaId(1L);
        logEsperado.setPrecioVenta(100.0);

        when(prendaRepository.findById(1L)).thenReturn(Optional.of(pantalon));
        when(prendaRepository.updateStock(eq(1L), eq(8))).thenReturn(pantalon);
        when(ventaRepository.save(any(LogVenta.class))).thenReturn(logEsperado);

        // Act
        LogVenta resultado = ventaService.realizarVenta(1L, 2);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getPrendaId());
        assertEquals(100.0, resultado.getPrecioVenta());
        verify(prendaRepository, times(1)).updateStock(1L, 8);
        verify(ventaRepository, times(1)).save(any(LogVenta.class));
    }

    // -------------------------------------------------------------------------
    // TC-VS-02: Stock insuficiente lanza excepción
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-VS-02: Stock insuficiente – lanza RuntimeException")
    void shouldThrowExceptionWhenInsufficientStock_IntegrityValidated() {
        // Arrange
        Pantalon pantalon = new Pantalon(1L, "Nike", "M", 50.0, 1);
        when(prendaRepository.findById(1L)).thenReturn(Optional.of(pantalon));

        // Act & Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> ventaService.realizarVenta(1L, 5)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("stock") ||
                   ex.getMessage().toLowerCase().contains("insuf"));
        verify(ventaRepository, never()).save(any());
    }

    // -------------------------------------------------------------------------
    // TC-VS-03: Prenda no encontrada lanza excepción
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-VS-03: Prenda inexistente – lanza RuntimeException")
    void shouldThrowExceptionWhenPrendaNotFound() {
        // Arrange
        when(prendaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> ventaService.realizarVenta(99L, 1)
        );
        assertNotNull(ex.getMessage());
        verify(ventaRepository, never()).save(any());
    }

    // -------------------------------------------------------------------------
    // TC-VS-04: Venta exitosa con Camisa
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-VS-04: Venta exitosa con Camisa – precio calculado correctamente")
    void shouldCalculatePriceCorrectlyForCamisa() {
        // Arrange
        Camisa camisa = new Camisa(2L, "Adidas", "L", 30.0, 5, Camisa.TipoEstampado.BORDADO);
        LogVenta logEsperado = new LogVenta();
        logEsperado.setPrendaId(2L);
        logEsperado.setPrecioVenta(90.0); // 30.0 * 3

        when(prendaRepository.findById(2L)).thenReturn(Optional.of(camisa));
        when(prendaRepository.updateStock(eq(2L), eq(2))).thenReturn(camisa);
        when(ventaRepository.save(any(LogVenta.class))).thenReturn(logEsperado);

        // Act
        LogVenta resultado = ventaService.realizarVenta(2L, 3);

        // Assert
        assertEquals(90.0, resultado.getPrecioVenta(), 0.001);
        verify(prendaRepository).updateStock(2L, 2);
    }

    // -------------------------------------------------------------------------
    // TC-VS-05: Venta de exactamente el total del stock disponible
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-VS-05: Venta igual al stock total – operación válida")
    void shouldAllowSaleWhenQuantityEqualsStock() {
        // Arrange
        Pantalon pantalon = new Pantalon(3L, "Puma", "S", 20.0, 4);
        LogVenta logEsperado = new LogVenta();
        logEsperado.setPrendaId(3L);
        logEsperado.setPrecioVenta(80.0);

        when(prendaRepository.findById(3L)).thenReturn(Optional.of(pantalon));
        when(prendaRepository.updateStock(eq(3L), eq(0))).thenReturn(pantalon);
        when(ventaRepository.save(any(LogVenta.class))).thenReturn(logEsperado);

        // Act
        LogVenta resultado = ventaService.realizarVenta(3L, 4);

        // Assert
        assertNotNull(resultado);
        assertEquals(80.0, resultado.getPrecioVenta(), 0.001);
        verify(prendaRepository).updateStock(3L, 0);
    }
}
