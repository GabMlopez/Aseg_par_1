package com.empresa.tienda;

import com.empresa.tienda.application.services.VentaService;
import com.empresa.tienda.domain.model.Pantalon;
import com.empresa.tienda.domain.ports.LogVentaRepositoryPort;
import com.empresa.tienda.domain.ports.PrendaRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {
        @Mock
        private PrendaRepositoryPort prendaRepository;

        @Mock
        private LogVentaRepositoryPort ventaRepository;

        @InjectMocks
        private VentaService ventaService;

        @Test
        void shouldCreateSaleSuccessfully_CorrectnessValidated() {
            // Arrange
            Pantalon pantalon = new Pantalon(1L, "Nike", "M", 50.0, 10);
            when(prendaRepository.findById(1L)).thenReturn(Optional.of(pantalon));
            when(prendaRepository.updateStock(eq(1L), eq(8))).thenReturn(pantalon);

            // Act
            var venta = ventaService.realizarVenta(1L, 2);

            // Assert (Correctness & Reliability)
            assertNotNull(venta);
            assertEquals(1L, venta.getPrendaId());
            assertEquals(100.0, venta.getPrecioVenta());
            verify(prendaRepository, times(1)).updateStock(1L, 8);
            verify(ventaRepository, times(1)).save(any());
        }

        @Test
        void shouldThrowExceptionWhenInsufficientStock_IntegrityValidated() {
            Pantalon pantalon = new Pantalon(1L, "Nike", "M", 50.0, 1);
            when(prendaRepository.findById(1L)).thenReturn(Optional.of(pantalon));

            assertThrows(   RuntimeException.class, () -> ventaService.realizarVenta(1L, 5));
        }
}
