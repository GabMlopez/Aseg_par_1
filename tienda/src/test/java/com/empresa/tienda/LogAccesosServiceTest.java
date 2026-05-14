package com.empresa.tienda;

import com.empresa.tienda.application.services.LogAccesosService;
import com.empresa.tienda.domain.model.LogAcceso;
import com.empresa.tienda.domain.ports.LogAccesoRepositoryPort;
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
@DisplayName("LogAccesosService - Pruebas Unitarias")
class LogAccesosServiceTest {

    @Mock
    private LogAccesoRepositoryPort logAccesoRepository;

    @InjectMocks
    private LogAccesosService logAccesosService;

    // -------------------------------------------------------------------------
    // TC-LA-01: createLog persiste el log y lo retorna
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-LA-01: createLog() guarda el acceso y retorna la entidad persistida")
    void shouldCreateLogSuccessfully() {
        // Arrange
        LogAcceso acceso = new LogAcceso(null, 1L, "LOGIN", LocalDateTime.now());
        LogAcceso guardado = new LogAcceso(100L, 1L, "LOGIN", acceso.getFecha());
        when(logAccesoRepository.save(acceso)).thenReturn(guardado);

        // Act
        LogAcceso resultado = logAccesosService.createLog(acceso);

        // Assert
        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());
        assertEquals("LOGIN", resultado.getActividad());
        verify(logAccesoRepository, times(1)).save(acceso);
    }

    // -------------------------------------------------------------------------
    // TC-LA-02: findByUsuarioId retorna logs del usuario correcto
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-LA-02: findByUsuarioId() retorna solo los logs del usuario indicado")
    void shouldReturnLogsByUsuarioId() {
        // Arrange
        Long usuarioId = 5L;
        List<LogAcceso> logs = List.of(
                new LogAcceso(1L, usuarioId, "LOGIN", LocalDateTime.now()),
                new LogAcceso(2L, usuarioId, "LOGOUT", LocalDateTime.now())
        );
        when(logAccesoRepository.findByUsuarioId(usuarioId)).thenReturn(logs);

        // Act
        List<LogAcceso> resultado = logAccesosService.findByUsuarioId(usuarioId);

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(l -> l.getUsuarioId().equals(usuarioId)));
        verify(logAccesoRepository, times(1)).findByUsuarioId(usuarioId);
    }

    // -------------------------------------------------------------------------
    // TC-LA-03: findByUsuarioId retorna lista vacía si no hay logs
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-LA-03: findByUsuarioId() retorna lista vacía cuando no hay registros")
    void shouldReturnEmptyWhenNoLogsForUser() {
        // Arrange
        when(logAccesoRepository.findByUsuarioId(99L)).thenReturn(List.of());

        // Act
        List<LogAcceso> resultado = logAccesosService.findByUsuarioId(99L);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // -------------------------------------------------------------------------
    // TC-LA-04: findByDate retorna logs en la fecha especificada
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-LA-04: findByDate() retorna los logs registrados en la fecha dada")
    void shouldReturnLogsByDate() {
        // Arrange
        LocalDateTime fecha = LocalDateTime.of(2025, 5, 10, 8, 0);
        List<LogAcceso> logs = List.of(
                new LogAcceso(3L, 1L, "ACCESS", fecha),
                new LogAcceso(4L, 2L, "ACCESS", fecha)
        );
        when(logAccesoRepository.findByDate(fecha)).thenReturn(logs);

        // Act
        List<LogAcceso> resultado = logAccesosService.findByDate(fecha);

        // Assert
        assertEquals(2, resultado.size());
        verify(logAccesoRepository, times(1)).findByDate(fecha);
    }

    // -------------------------------------------------------------------------
    // TC-LA-05: findByDate retorna lista vacía si no hay registros en esa fecha
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-LA-05: findByDate() retorna lista vacía si no hay logs en esa fecha")
    void shouldReturnEmptyWhenNoLogsForDate() {
        // Arrange
        LocalDateTime fechaSinLogs = LocalDateTime.of(2000, 1, 1, 0, 0);
        when(logAccesoRepository.findByDate(fechaSinLogs)).thenReturn(List.of());

        // Act
        List<LogAcceso> resultado = logAccesosService.findByDate(fechaSinLogs);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
