package com.empresa.tienda;

import com.empresa.tienda.application.services.VentaService;
import com.empresa.tienda.domain.model.LogVenta;
import com.empresa.tienda.interfaces.rest.ventaController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ventaController - Pruebas Unitarias")
class VentaControllerTest {

    @Mock
    private VentaService ventaService;

    @InjectMocks
    private ventaController ventaController;

    // -------------------------------------------------------------------------
    // TC-VC-01: vender() – venta exitosa retorna el LogVenta del servicio
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-VC-01: vender() – delega en VentaService y retorna el LogVenta")
    void shouldDelegateToServiceAndReturnLogVenta() {
        // Arrange
        LogVenta logEsperado = new LogVenta(1L, 10L, 150.0, LocalDateTime.now());
        when(ventaService.realizarVenta(10L, 3)).thenReturn(logEsperado);

        // Act
        LogVenta resultado = ventaController.vender(10L, 3);

        // Assert
        assertNotNull(resultado);
        assertEquals(10L, resultado.getPrendaId());
        assertEquals(150.0, resultado.getPrecioVenta(), 0.001);
        verify(ventaService, times(1)).realizarVenta(10L, 3);
    }

    // -------------------------------------------------------------------------
    // TC-VC-02: vender() – propaga la excepción lanzada por el servicio
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-VC-02: vender() – propaga RuntimeException del servicio (stock insuficiente)")
    void shouldPropagateExceptionFromService() {
        // Arrange
        when(ventaService.realizarVenta(99L, 100))
                .thenThrow(new RuntimeException("Stock INsuficiente"));

        // Act & Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> ventaController.vender(99L, 100)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("stock"));
        verify(ventaService, times(1)).realizarVenta(99L, 100);
    }

    // -------------------------------------------------------------------------
    // TC-VC-03: vender() – cantidad 1, stock mínimo válido
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-VC-03: vender() – cantidad = 1 invoca el servicio exactamente una vez")
    void shouldCallServiceOnceForMinimumQuantity() {
        // Arrange
        LogVenta log = new LogVenta(2L, 5L, 50.0, LocalDateTime.now());
        when(ventaService.realizarVenta(5L, 1)).thenReturn(log);

        // Act
        LogVenta resultado = ventaController.vender(5L, 1);

        // Assert
        assertEquals(50.0, resultado.getPrecioVenta(), 0.001);
        verify(ventaService, times(1)).realizarVenta(5L, 1);
    }
}
