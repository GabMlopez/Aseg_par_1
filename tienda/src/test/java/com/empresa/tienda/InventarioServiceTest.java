package com.empresa.tienda;

import com.empresa.tienda.application.services.InventarioService;
import com.empresa.tienda.domain.model.Camisa;
import com.empresa.tienda.domain.model.Pantalon;
import com.empresa.tienda.domain.model.Prenda;
import com.empresa.tienda.domain.ports.PrendaRepositoryPort;
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
@DisplayName("InventarioService - Pruebas Unitarias")
class InventarioServiceTest {

    @Mock
    private PrendaRepositoryPort prendaRepository;

    @InjectMocks
    private InventarioService inventarioService;

    // -------------------------------------------------------------------------
    // TC-IS-01: Listar prendas retorna lista completa
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-IS-01: listarPrendas() retorna todas las prendas del repositorio")
    void shouldReturnAllPrendas() {
        // Arrange
        List<Prenda> prendas = List.of(
                new Pantalon(1L, "Nike", "M", 50.0, 10),
                new Camisa(2L, "Adidas", "L", 30.0, 5, Camisa.TipoEstampado.PLASTICO)
        );
        when(prendaRepository.findAll()).thenReturn(prendas);

        // Act
        List<Prenda> resultado = inventarioService.listarPrendas();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(prendaRepository, times(1)).findAll();
    }

    // -------------------------------------------------------------------------
    // TC-IS-02: Listar prendas con inventario vacío
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-IS-02: listarPrendas() retorna lista vacía cuando no hay prendas")
    void shouldReturnEmptyListWhenNoProducts() {
        // Arrange
        when(prendaRepository.findAll()).thenReturn(List.of());

        // Act
        List<Prenda> resultado = inventarioService.listarPrendas();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // -------------------------------------------------------------------------
    // TC-IS-03: Crear prenda guarda y retorna la entidad
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-IS-03: crearPrenda() persiste y retorna la prenda creada")
    void shouldCreateAndReturnPrenda() {
        // Arrange
        Pantalon nueva = new Pantalon(null, "Puma", "XL", 45.0, 20);
        Pantalon guardada = new Pantalon(5L, "Puma", "XL", 45.0, 20);
        when(prendaRepository.save(nueva)).thenReturn(guardada);

        // Act
        Prenda resultado = inventarioService.crearPrenda(nueva);

        // Assert
        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals("Puma", resultado.getMarca());
        verify(prendaRepository, times(1)).save(nueva);
    }

    // -------------------------------------------------------------------------
    // TC-IS-04: Actualizar stock invoca al repositorio correctamente
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-IS-04: actualizarStock() delega correctamente en el repositorio")
    void shouldUpdateStockCorrectly() {
        // Arrange
        Pantalon actualizado = new Pantalon(1L, "Nike", "M", 50.0, 25);
        when(prendaRepository.updateStock(1L, 25)).thenReturn(actualizado);

        // Act
        Prenda resultado = inventarioService.actualizarStock(1L, 25);

        // Assert
        assertEquals(25, resultado.getCantidad());
        verify(prendaRepository, times(1)).updateStock(1L, 25);
    }

    // -------------------------------------------------------------------------
    // TC-IS-05: Eliminar prenda invoca deleteById exactamente una vez
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-IS-05: eliminarPrenda() llama a deleteById del repositorio")
    void shouldDeletePrendaById() {
        // Arrange
        doNothing().when(prendaRepository).deleteById(1L);

        // Act
        inventarioService.eliminarPrenda(1L);

        // Assert
        verify(prendaRepository, times(1)).deleteById(1L);
    }

    // -------------------------------------------------------------------------
    // TC-IS-06: crearPrenda con tipo Camisa con estampado BORDADO
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-IS-06: crearPrenda() funciona con Camisa de tipo BORDADO")
    void shouldCreateCamisaWithEstampado() {
        // Arrange
        Camisa camisa = new Camisa(null, "Lacoste", "M", 80.0, 15, Camisa.TipoEstampado.BORDADO);
        Camisa guardada = new Camisa(10L, "Lacoste", "M", 80.0, 15, Camisa.TipoEstampado.BORDADO);
        when(prendaRepository.save(camisa)).thenReturn(guardada);

        // Act
        Prenda resultado = inventarioService.crearPrenda(camisa);

        // Assert
        assertEquals(10L, resultado.getId());
        assertEquals("CAMISA_BORDADO", resultado.getTipo());
    }
}
