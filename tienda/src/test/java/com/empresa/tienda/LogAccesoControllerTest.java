package com.empresa.tienda;

import com.empresa.tienda.application.services.LogAccesosService;
import com.empresa.tienda.domain.model.LogAcceso;
import com.empresa.tienda.interfaces.rest.LogAccesoController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LogAccesoController - Pruebas Unitarias")
class LogAccesoControllerTest {

    @Mock
    private LogAccesosService logAccesosService;

    @InjectMocks
    private LogAccesoController logAccesoController;

    // -------------------------------------------------------------------------
    // TC-LC-01: logsByUserId() – retorna lista de logs del usuario
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-LC-01: logsByUserId() – delega en el servicio y retorna logs del usuario")
    void shouldReturnLogsByUserId() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        List<LogAcceso> logs = List.of(
                new LogAcceso(1L, 5L, "LOGIN", now),
                new LogAcceso(2L, 5L, "LOGOUT", now)
        );
        when(logAccesosService.findByUsuarioId(5L)).thenReturn(logs);

        // Act
        List<LogAcceso> resultado = logAccesoController.logsByUserId(5L);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("LOGIN", resultado.get(0).getActividad());
        assertEquals("LOGOUT", resultado.get(1).getActividad());
        verify(logAccesosService, times(1)).findByUsuarioId(5L);
    }

    // -------------------------------------------------------------------------
    // TC-LC-02: logsByUserId() – usuario sin logs retorna lista vacía
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-LC-02: logsByUserId() – usuario sin registros retorna lista vacía")
    void shouldReturnEmptyListWhenUserHasNoLogs() {
        // Arrange
        when(logAccesosService.findByUsuarioId(99L)).thenReturn(List.of());

        // Act
        List<LogAcceso> resultado = logAccesoController.logsByUserId(99L);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // -------------------------------------------------------------------------
    // TC-LC-03: logsByDate() – retorna logs filtrados por fecha
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-LC-03: logsByDate() – delega en el servicio y retorna logs de la fecha dada")
    void shouldReturnLogsByDate() {
        // Arrange
        LocalDateTime fecha = LocalDateTime.of(2026, 5, 13, 10, 0, 0);
        List<LogAcceso> logs = List.of(
                new LogAcceso(3L, 7L, "LOGIN", fecha)
        );
        when(logAccesosService.findByDate(fecha)).thenReturn(logs);

        // Act
        List<LogAcceso> resultado = logAccesoController.logsByDate(fecha);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("LOGIN", resultado.get(0).getActividad());
        verify(logAccesosService, times(1)).findByDate(fecha);
    }

    // -------------------------------------------------------------------------
    // TC-LC-04: logsByDate() – fecha sin registros retorna lista vacía
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-LC-04: logsByDate() – fecha sin logs retorna lista vacía")
    void shouldReturnEmptyListForDateWithNoLogs() {
        // Arrange
        LocalDateTime fecha = LocalDateTime.of(2026, 1, 1, 0, 0, 0);
        when(logAccesosService.findByDate(fecha)).thenReturn(List.of());

        // Act
        List<LogAcceso> resultado = logAccesoController.logsByDate(fecha);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // -------------------------------------------------------------------------
    // TC-LC-05: createLog() – crea y retorna el LogAcceso guardado
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-LC-05: createLog() – delega en el servicio y retorna la entidad persistida")
    void shouldCreateAndReturnLogAcceso() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        LogAcceso entrada = new LogAcceso(null, 3L, "COMPRA", null);
        LogAcceso guardado = new LogAcceso(10L, 3L, "COMPRA", now);
        when(logAccesosService.createLog(any(LogAcceso.class))).thenReturn(guardado);

        // Act
        LogAcceso resultado = logAccesoController.createLog(entrada);

        // Assert
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals(3L, resultado.getUsuarioId());
        assertEquals("COMPRA", resultado.getActividad());
        verify(logAccesosService, times(1)).createLog(entrada);
    }
}
