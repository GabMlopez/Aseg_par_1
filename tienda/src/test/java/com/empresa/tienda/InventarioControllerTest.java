package com.empresa.tienda;

import com.empresa.tienda.application.services.InventarioService;
import com.empresa.tienda.domain.model.Camisa;
import com.empresa.tienda.domain.model.Pantalon;
import com.empresa.tienda.domain.model.Prenda;
import com.empresa.tienda.interfaces.rest.InventarioController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InventarioController - Pruebas Unitarias")
class InventarioControllerTest {

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private InventarioController inventarioController;

    // -------------------------------------------------------------------------
    // TC-IC-01: listar() – retorna todas las prendas del servicio
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-IC-01: listar() – delega en InventarioService y retorna la lista completa")
    void shouldReturnAllPrendas() {
        // Arrange
        List<Prenda> prendas = List.of(
                new Pantalon(1L, "Nike", "M", 50.0, 10),
                new Camisa(2L, "Adidas", "L", 30.0, 5, Camisa.TipoEstampado.PLASTICO)
        );
        when(inventarioService.listarPrendas()).thenReturn(prendas);

        // Act
        List<Prenda> resultado = inventarioController.listar();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Nike", resultado.get(0).getMarca());
        verify(inventarioService, times(1)).listarPrendas();
    }

    // -------------------------------------------------------------------------
    // TC-IC-02: listar() – inventario vacío retorna lista vacía
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-IC-02: listar() – retorna lista vacía cuando no hay prendas")
    void shouldReturnEmptyListWhenNoProducts() {
        // Arrange
        when(inventarioService.listarPrendas()).thenReturn(List.of());

        // Act
        List<Prenda> resultado = inventarioController.listar();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // -------------------------------------------------------------------------
    // TC-IC-03: crearCamisa() – persiste y retorna la Camisa guardada
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-IC-03: crearCamisa() – delega en el servicio y retorna Camisa con id asignado")
    void shouldCreateAndReturnCamisa() {
        // Arrange
        Camisa entrada = new Camisa(null, "Lacoste", "M", 80.0, 15, Camisa.TipoEstampado.BORDADO);
        Camisa guardada = new Camisa(10L, "Lacoste", "M", 80.0, 15, Camisa.TipoEstampado.BORDADO);
        when(inventarioService.crearPrenda(any(Camisa.class))).thenReturn(guardada);

        // Act
        Camisa resultado = inventarioController.crearCamisa(entrada);

        // Assert
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("CAMISA_BORDADO", resultado.getTipo());
        verify(inventarioService, times(1)).crearPrenda(entrada);
    }

    // -------------------------------------------------------------------------
    // TC-IC-04: crearPantalon() – persiste y retorna el Pantalon guardado
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-IC-04: crearPantalon() – delega en el servicio y retorna Pantalon con id asignado")
    void shouldCreateAndReturnPantalon() {
        // Arrange
        Pantalon entrada = new Pantalon(null, "Puma", "XL", 45.0, 20);
        Pantalon guardado = new Pantalon(5L, "Puma", "XL", 45.0, 20);
        when(inventarioService.crearPrenda(any(Pantalon.class))).thenReturn(guardado);

        // Act
        Pantalon resultado = inventarioController.crearPantalon(entrada);

        // Assert
        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals("Puma", resultado.getMarca());
        assertEquals(20, resultado.getCantidad());
        verify(inventarioService, times(1)).crearPrenda(entrada);
    }

    // -------------------------------------------------------------------------
    // TC-IC-05: actualizarStock() – invoca el servicio y retorna la prenda actualizada
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-IC-05: actualizarStock() – delega en el servicio y retorna la prenda con nuevo stock")
    void shouldUpdateStockAndReturnUpdatedPrenda() {
        // Arrange
        Pantalon actualizado = new Pantalon(1L, "Nike", "M", 50.0, 25);
        when(inventarioService.actualizarStock(1L, 25)).thenReturn(actualizado);

        // Act
        Prenda resultado = inventarioController.actualizarStock(1L, 25);

        // Assert
        assertNotNull(resultado);
        assertEquals(25, resultado.getCantidad());
        verify(inventarioService, times(1)).actualizarStock(1L, 25);
    }

    // -------------------------------------------------------------------------
    // TC-IC-06: crearCamisa() con estampado PLASTICO
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-IC-06: crearCamisa() – funciona con TipoEstampado PLASTICO")
    void shouldCreateCamisaWithEstampadoPlastico() {
        // Arrange
        Camisa entrada = new Camisa(null, "Tommy", "S", 55.0, 8, Camisa.TipoEstampado.PLASTICO);
        Camisa guardada = new Camisa(7L, "Tommy", "S", 55.0, 8, Camisa.TipoEstampado.PLASTICO);
        when(inventarioService.crearPrenda(any(Camisa.class))).thenReturn(guardada);

        // Act
        Camisa resultado = inventarioController.crearCamisa(entrada);

        // Assert
        assertEquals(7L, resultado.getId());
        assertEquals("CAMISA_PLASTICO", resultado.getTipo());
    }
}
