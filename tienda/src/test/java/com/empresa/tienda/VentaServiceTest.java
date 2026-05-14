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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
    private LogVentaRepositoryPort logVentaRepository;

    @InjectMocks
    private VentaService ventaService;

    // -------------------------------------------------------------------------
    // TC-VS-01: Venta exitosa con Pantalón – rama principal (stock suficiente)
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
        when(logVentaRepository.save(any(LogVenta.class))).thenReturn(logEsperado);

        // Act
        LogVenta resultado = ventaService.realizarVenta(1L, 2);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getPrendaId());
        assertEquals(100.0, resultado.getPrecioVenta(), 0.001);
        verify(prendaRepository, times(1)).updateStock(1L, 8);
        verify(logVentaRepository, times(1)).save(any(LogVenta.class));
    }

    // -------------------------------------------------------------------------
    // TC-VS-02: Stock insuficiente – rama cantidad < stock → lanza excepción
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-VS-02: Stock insuficiente – lanza RuntimeException con mensaje adecuado")
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
                   ex.getMessage().toLowerCase().contains("insuf"),
                   "El mensaje debe indicar stock insuficiente");
        // No se debe registrar ninguna venta
        verify(logVentaRepository, never()).save(any());
    }

    // -------------------------------------------------------------------------
    // TC-VS-03: Prenda no encontrada – rama Optional.empty() → lanza excepción
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
        assertNotNull(ex.getMessage(), "Debe tener un mensaje de error");
        assertTrue(ex.getMessage().toLowerCase().contains("prenda") ||
                   ex.getMessage().toLowerCase().contains("encontrada"),
                   "El mensaje debe indicar que la prenda no fue encontrada");
        verify(prendaRepository, never()).updateStock(any(), any());
        verify(logVentaRepository, never()).save(any());
    }

    // -------------------------------------------------------------------------
    // TC-VS-04: Venta exitosa con Camisa – precio calculado correctamente
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-VS-04: Venta exitosa con Camisa – precio = precio_unitario * cantidad")
    void shouldCalculatePriceCorrectlyForCamisa() {
        // Arrange
        Camisa camisa = new Camisa(2L, "Adidas", "L", 30.0, 5, Camisa.TipoEstampado.BORDADO);
        LogVenta logEsperado = new LogVenta();
        logEsperado.setPrendaId(2L);
        logEsperado.setPrecioVenta(90.0); // 30.0 * 3

        when(prendaRepository.findById(2L)).thenReturn(Optional.of(camisa));
        when(prendaRepository.updateStock(eq(2L), eq(2))).thenReturn(camisa);
        when(logVentaRepository.save(any(LogVenta.class))).thenReturn(logEsperado);

        // Act
        LogVenta resultado = ventaService.realizarVenta(2L, 3);

        // Assert
        assertEquals(90.0, resultado.getPrecioVenta(), 0.001);
        verify(prendaRepository).updateStock(2L, 2);
    }

    // -------------------------------------------------------------------------
    // TC-VS-05: Venta de exactamente el total del stock disponible (límite)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-VS-05: Venta igual al stock total – operación válida (stock resultante = 0)")
    void shouldAllowSaleWhenQuantityEqualsStock() {
        // Arrange
        Pantalon pantalon = new Pantalon(3L, "Puma", "S", 20.0, 4);
        LogVenta logEsperado = new LogVenta();
        logEsperado.setPrendaId(3L);
        logEsperado.setPrecioVenta(80.0);

        when(prendaRepository.findById(3L)).thenReturn(Optional.of(pantalon));
        when(prendaRepository.updateStock(eq(3L), eq(0))).thenReturn(pantalon);
        when(logVentaRepository.save(any(LogVenta.class))).thenReturn(logEsperado);

        // Act
        LogVenta resultado = ventaService.realizarVenta(3L, 4);

        // Assert
        assertNotNull(resultado);
        assertEquals(80.0, resultado.getPrecioVenta(), 0.001);
        verify(prendaRepository).updateStock(3L, 0);
    }

    // -------------------------------------------------------------------------
    // TC-VS-06: LogVenta creado contiene prendaId, precioVenta y fechaGeneracion
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-VS-06: LogVenta guardado tiene prendaId, precioVenta y fechaGeneracion correctos")
    void shouldSetAllFieldsOnLogVenta() {
        // Arrange
        Pantalon pantalon = new Pantalon(5L, "Reebok", "XL", 40.0, 6);
        when(prendaRepository.findById(5L)).thenReturn(Optional.of(pantalon));
        when(prendaRepository.updateStock(eq(5L), eq(4))).thenReturn(pantalon);

        ArgumentCaptor<LogVenta> captor = ArgumentCaptor.forClass(LogVenta.class);
        LogVenta logDevuelto = new LogVenta();
        logDevuelto.setPrendaId(5L);
        logDevuelto.setPrecioVenta(80.0);
        when(logVentaRepository.save(captor.capture())).thenReturn(logDevuelto);

        LocalDateTime antes = LocalDateTime.now().minusSeconds(1);

        // Act
        ventaService.realizarVenta(5L, 2);

        // Assert – verificamos los campos del LogVenta enviado al repositorio
        LogVenta logCapturado = captor.getValue();
        assertEquals(5L, logCapturado.getPrendaId(),
                "prendaId debe coincidir con la prenda vendida");
        assertEquals(80.0, logCapturado.getPrecioVenta(), 0.001,
                "precioVenta = precio_unitario (40.0) * cantidad (2)");
        assertNotNull(logCapturado.getFechaGeneracion(),
                "fechaGeneracion no debe ser null");
        assertFalse(logCapturado.getFechaGeneracion().isBefore(antes),
                "fechaGeneracion debe ser posterior al inicio del test");
    }

    // -------------------------------------------------------------------------
    // TC-VS-07: Stock exactamente 0 – no permite venta (límite inferior estricto)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-VS-07: Stock = 0 al intentar vender 1 unidad – lanza RuntimeException")
    void shouldThrowWhenStockIsZeroAndQuantityIsOne() {
        // Arrange
        Pantalon pantalon = new Pantalon(4L, "Fila", "M", 25.0, 0);
        when(prendaRepository.findById(4L)).thenReturn(Optional.of(pantalon));

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> ventaService.realizarVenta(4L, 1),
                "Debe lanzar excepción cuando el stock es 0");
        verify(logVentaRepository, never()).save(any());
    }

    // -------------------------------------------------------------------------
    // TC-VS-08: Venta con Camisa de estampado PLASTICO – flujo completo
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-VS-08: Venta con Camisa PLASTICO – stock actualizado y LogVenta registrado")
    void shouldSellCamisaWithEstampadoPlastico() {
        // Arrange
        Camisa camisa = new Camisa(6L, "Polo", "S", 60.0, 10, Camisa.TipoEstampado.PLASTICO);
        LogVenta logEsperado = new LogVenta();
        logEsperado.setPrendaId(6L);
        logEsperado.setPrecioVenta(120.0); // 60.0 * 2

        when(prendaRepository.findById(6L)).thenReturn(Optional.of(camisa));
        when(prendaRepository.updateStock(eq(6L), eq(8))).thenReturn(camisa);
        when(logVentaRepository.save(any(LogVenta.class))).thenReturn(logEsperado);

        // Act
        LogVenta resultado = ventaService.realizarVenta(6L, 2);

        // Assert
        assertNotNull(resultado);
        assertEquals(120.0, resultado.getPrecioVenta(), 0.001);
        verify(prendaRepository, times(1)).updateStock(6L, 8);
        verify(logVentaRepository, times(1)).save(any(LogVenta.class));
    }
}