package com.empresa.tienda;

import com.empresa.tienda.application.services.AuthService;
import com.empresa.tienda.domain.model.Usuario;
import com.empresa.tienda.domain.ports.UserRepositoryPort;
import com.empresa.tienda.infrastructure.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService - Pruebas Unitarias")
class AuthServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    // -------------------------------------------------------------------------
    // TC-AS-01: Login exitoso retorna un token JWT
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-AS-01: login() exitoso retorna token JWT no nulo")
    void shouldReturnTokenOnSuccessfulLogin() {
        // Arrange
        Usuario usuario = new Usuario(1L, "mateo", "hashed_pass", "Mateo", null);
        when(userRepository.findByUsername("mateo")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("pass123", "hashed_pass")).thenReturn(true);
        when(userRepository.update(any(Usuario.class))).thenReturn(usuario);
        when(tokenProvider.generarToken("mateo")).thenReturn("jwt.token.aqui");

        // Act
        String token = authService.login("mateo", "pass123");

        // Assert
        assertNotNull(token);
        assertEquals("jwt.token.aqui", token);
        verify(userRepository, times(1)).update(any(Usuario.class));
        verify(tokenProvider, times(1)).generarToken("mateo");
    }

    // -------------------------------------------------------------------------
    // TC-AS-02: Login con usuario inexistente lanza excepción
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-AS-02: login() con usuario inexistente – lanza RuntimeException")
    void shouldThrowWhenUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("fantasma")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.login("fantasma", "cualquier")
        );
        assertNotNull(ex.getMessage());
        verify(tokenProvider, never()).generarToken(anyString());
    }

    // -------------------------------------------------------------------------
    // TC-AS-03: Login con contraseña incorrecta lanza excepción
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-AS-03: login() con contraseña incorrecta – lanza RuntimeException")
    void shouldThrowWhenPasswordMismatch() {
        // Arrange
        Usuario usuario = new Usuario(1L, "mateo", "hashed_pass", "Mateo", null);
        when(userRepository.findByUsername("mateo")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("mala_clave", "hashed_pass")).thenReturn(false);

        // Act & Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.login("mateo", "mala_clave")
        );
        assertNotNull(ex.getMessage());
        verify(tokenProvider, never()).generarToken(anyString());
        verify(userRepository, never()).update(any());
    }

    // -------------------------------------------------------------------------
    // TC-AS-04: Register codifica contraseña y guarda el usuario
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-AS-04: register() codifica la contraseña antes de guardar")
    void shouldEncodePasswordOnRegister() {
        // Arrange
        Usuario nuevoUsuario = new Usuario(null, "nuevousr", "plaintext", "Nuevo", null);
        Usuario guardado = new Usuario(5L, "nuevousr", "$2a$encoded", "Nuevo", null);
        when(passwordEncoder.encode("plaintext")).thenReturn("$2a$encoded");
        when(userRepository.save(any(Usuario.class))).thenReturn(guardado);

        // Act
        Usuario resultado = authService.register(nuevoUsuario);

        // Assert
        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        // Verificar que la contraseña en texto plano ya no se usa
        verify(passwordEncoder, times(1)).encode("plaintext");
        verify(userRepository, times(1)).save(any(Usuario.class));
    }

    // -------------------------------------------------------------------------
    // TC-AS-05: Register actualiza lastActive antes de guardar
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-AS-05: register() establece lastActive antes de persistir")
    void shouldSetLastActiveOnRegister() {
        // Arrange
        Usuario usuario = new Usuario(null, "usr2", "pass", "User2", null);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            assertNotNull(u.getLastActive(), "lastActive debe ser establecido antes de save");
            u.setId(10L);
            return u;
        });

        // Act
        Usuario resultado = authService.register(usuario);

        // Assert
        assertNotNull(resultado.getId());
    }
}
