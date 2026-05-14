package com.empresa.tienda;

import com.empresa.tienda.application.services.AuthService;
import com.empresa.tienda.domain.model.Usuario;
import com.empresa.tienda.interfaces.rest.AuthController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController - Pruebas Unitarias")
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    // -------------------------------------------------------------------------
    // TC-AC-01: login() – credenciales correctas retorna token
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-AC-01: login() – credenciales válidas retorna AuthResponse con token")
    void shouldReturnTokenOnSuccessfulLogin() {
        // Arrange
        when(authService.login("admin", "secret")).thenReturn("jwt-token-xyz");

        // Act
        AuthController.AuthResponse response = authController.login(
                new AuthController.LoginRequest("admin", "secret")
        );

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token-xyz", response.token());
        verify(authService, times(1)).login("admin", "secret");
    }

    // -------------------------------------------------------------------------
    // TC-AC-02: login() – usuario no registrado lanza excepción
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-AC-02: login() – usuario inexistente propaga RuntimeException")
    void shouldPropagateExceptionWhenUserNotFound() {
        // Arrange
        when(authService.login("inexistente", "pass"))
                .thenThrow(new RuntimeException("Usuario no registrado"));

        // Act & Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authController.login(new AuthController.LoginRequest("inexistente", "pass"))
        );
        assertTrue(ex.getMessage().contains("no registrado"));
        verify(authService, times(1)).login("inexistente", "pass");
    }

    // -------------------------------------------------------------------------
    // TC-AC-03: login() – contraseña incorrecta lanza excepción
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-AC-03: login() – contraseña incorrecta propaga RuntimeException")
    void shouldPropagateExceptionWhenWrongPassword() {
        // Arrange
        when(authService.login("admin", "wrong"))
                .thenThrow(new RuntimeException("Contraseña incorrecta"));

        // Act & Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authController.login(new AuthController.LoginRequest("admin", "wrong"))
        );
        assertTrue(ex.getMessage().contains("incorrecta") || ex.getMessage().contains("Contraseña"));
    }

    // -------------------------------------------------------------------------
    // TC-AC-04: register() – registro exitoso retorna el usuario guardado
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-AC-04: register() – delega en AuthService y retorna el usuario creado")
    void shouldRegisterUserAndReturnSavedUser() {
        // Arrange
        Usuario entrada = new Usuario(null, "nuevo", "pass123", "Juan", null);
        Usuario guardado = new Usuario(1L, "nuevo", "$2a$bcrypt", "Juan", LocalDateTime.now());
        when(authService.register(any(Usuario.class))).thenReturn(guardado);

        // Act
        Usuario resultado = authController.register(entrada);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("nuevo", resultado.getUsername());
        assertEquals("Juan", resultado.getNombre());
        verify(authService, times(1)).register(entrada);
    }
}
