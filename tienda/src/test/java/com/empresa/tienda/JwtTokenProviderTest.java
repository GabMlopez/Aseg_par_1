package com.empresa.tienda;

import com.empresa.tienda.infrastructure.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtTokenProvider - Pruebas Unitarias")
class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;

    // Clave de al menos 64 bytes para HS512
    private static final String SECRET =
            "clave-super-secreta-para-pruebas-unitarias-con-512bits-12345678901234567890";
    private static final int EXPIRATION_MS = 3_600_000; // 1 hora

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret", SECRET);
        ReflectionTestUtils.setField(tokenProvider, "jwtExpiration", EXPIRATION_MS);
    }

    // -------------------------------------------------------------------------
    // TC-JWT-01: generarToken produce un token no nulo y no vacío
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-JWT-01: generarToken() retorna un token JWT válido no nulo")
    void shouldGenerateNonNullToken() {
        String token = tokenProvider.generarToken("mateo");
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    // -------------------------------------------------------------------------
    // TC-JWT-02: getUsernameFromToken recupera el sujeto correcto
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-JWT-02: getUsernameFromToken() extrae el username embebido en el token")
    void shouldExtractUsernameFromToken() {
        String token = tokenProvider.generarToken("admin");
        String username = tokenProvider.getUsernameFromToken(token);
        assertEquals("admin", username);
    }

    // -------------------------------------------------------------------------
    // TC-JWT-03: validateToken retorna true para token válido
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-JWT-03: validateToken() retorna true para un token recién generado")
    void shouldValidateCorrectToken() {
        String token = tokenProvider.generarToken("usuario_valido");
        assertTrue(tokenProvider.validateToken(token));
    }

    // -------------------------------------------------------------------------
    // TC-JWT-04: validateToken retorna false para token malformado
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-JWT-04: validateToken() retorna false para un token malformado")
    void shouldReturnFalseForMalformedToken() {
        assertFalse(tokenProvider.validateToken("esto.no.es.un.jwt.valido"));
    }

    // -------------------------------------------------------------------------
    // TC-JWT-05: validateToken retorna false para token vacío/nulo
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-JWT-05: validateToken() retorna false para string vacío")
    void shouldReturnFalseForEmptyToken() {
        assertFalse(tokenProvider.validateToken(""));
    }

    // -------------------------------------------------------------------------
    // TC-JWT-06: isTokenExpired retorna false para token vigente
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-JWT-06: isTokenExpired() retorna false para token aún vigente")
    void shouldReturnFalseForNonExpiredToken() {
        String token = tokenProvider.generarToken("usuario");
        assertFalse(tokenProvider.isTokenExpired(token));
    }

    // -------------------------------------------------------------------------
    // TC-JWT-07: Token expirado – isTokenExpired retorna true
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-JWT-07: isTokenExpired() retorna true para token con expiración 0ms")
    void shouldReturnTrueForExpiredToken() throws InterruptedException {
        // Crear provider con expiración de 1 ms
        JwtTokenProvider expiredProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(expiredProvider, "jwtSecret", SECRET);
        ReflectionTestUtils.setField(expiredProvider, "jwtExpiration", 1);

        String token = expiredProvider.generarToken("usuario");
        Thread.sleep(10); // Esperar que expire

        assertTrue(expiredProvider.isTokenExpired(token));
    }

    // -------------------------------------------------------------------------
    // TC-JWT-08: Tokens de distintos usuarios son diferentes
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-JWT-08: Dos tokens generados para distintos usuarios son únicos")
    void shouldGenerateUniqueTokensForDifferentUsers() {
        String token1 = tokenProvider.generarToken("usuario1");
        String token2 = tokenProvider.generarToken("usuario2");
        assertNotEquals(token1, token2);
    }

    // -------------------------------------------------------------------------
    // TC-JWT-09: validateToken retorna false con firma incorrecta (clave distinta)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("TC-JWT-09: validateToken() retorna false si el token fue firmado con otra clave")
    void shouldReturnFalseForTokenSignedWithDifferentKey() {
        // Crear token con clave diferente
        JwtTokenProvider otroProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(otroProvider, "jwtSecret",
                "clave-totalmente-diferente-para-firmar-el-token-en-la-prueba-9999999999");
        ReflectionTestUtils.setField(otroProvider, "jwtExpiration", EXPIRATION_MS);

        String tokenConOtraClave = otroProvider.generarToken("hacker");
        assertFalse(tokenProvider.validateToken(tokenConOtraClave));
    }
}
