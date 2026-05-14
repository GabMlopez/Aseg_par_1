package com.empresa.tienda.integration;

import com.empresa.tienda.domain.model.Usuario;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas de Integración - API REST con RestClient")
class TiendaApiIntegrationTest {

    @LocalServerPort
    private int port;

    private RestClient restClient;
    private String authToken;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

        if (authToken == null) {
            try {
                // 1. Registrar usuario de prueba
                Usuario newUser = new Usuario(null, "testuser", "password123", "Test User", null);
                restClient.post()
                        .uri("/auth/register")
                        .body(newUser)
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                            System.err.println("❌ Error registrando usuario: " + response.getStatusCode());
                        })
                        .body(Usuario.class);

                // 2. Login para obtener token
                var loginRequest = Map.of("username", "testuser", "password", "password123");
                Map<String, Object> loginResponse = restClient.post()
                        .uri("/auth")
                        .body(loginRequest)
                        .retrieve()
                        .body(new ParameterizedTypeReference<Map<String, Object>>() {});

                if (loginResponse != null && loginResponse.containsKey("token")) {
                    authToken = (String) loginResponse.get("token");
                    System.out.println("✅ Token obtenido: " + authToken.substring(0, Math.min(30, authToken.length())) + "...");
                }
            } catch (Exception e) {
                System.err.println("❌ Error en setup: " + e.getMessage());
            }
        }
    }

    // ========================================================================
    // Pruebas de Autenticación (Endpoints Públicos)
    // ========================================================================

    @Test
    @Order(1)
    @DisplayName("POST /auth/register - Registrar usuario nuevo")
    void shouldRegisterUser() {
        Usuario newUser = new Usuario(null, "nuevo_usuario", "miclave123", "Juan Perez", null);

        Usuario response = restClient.post()
                .uri("/auth/register")
                .body(newUser)
                .retrieve()
                .body(Usuario.class);

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("nuevo_usuario");
        assertThat(response.getId()).isNotNull();
    }

    @Test
    @Order(2)
    @DisplayName("POST /auth - Login exitoso retorna token")
    void shouldLoginSuccessfully() {
        var loginRequest = Map.of("username", "testuser", "password", "password123");

        Map<String, Object> response = restClient.post()
                .uri("/auth")
                .body(loginRequest)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        assertThat(response).containsKey("token");
        assertThat((String) response.get("token")).isNotEmpty();
    }

    @Test
    @Order(3)
    @DisplayName("POST /auth - Credenciales incorrectas")
    void shouldFailWithWrongCredentials() {
        var loginRequest = Map.of("username", "testuser", "password", "wrongpassword");

        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            restClient.post()
                    .uri("/auth")
                    .body(loginRequest)
                    .retrieve()
                    .body(String.class);
        });
    }

    // ========================================================================
    // Pruebas de Inventario (Requieren Token)
    // ========================================================================

    @Test
    @Order(4)
    @DisplayName("GET /inventario - Sin token debe retornar 401")
    void shouldReturn401WhenNoToken() {
        RestClient clientWithoutAuth = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            clientWithoutAuth.get()
                    .uri("/inventario")
                    .retrieve()
                    .toBodilessEntity();
        });
    }

    @Test
    @Order(5)
    @DisplayName("GET /inventario - Con token debe retornar 200")
    void shouldListPrendasWithAuth() {
        assertThat(authToken).isNotNull();

        RestClient clientWithAuth = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultHeader("Authorization", "Bearer " + authToken)
                .build();

        Object[] response = clientWithAuth.get()
                .uri("/inventario")
                .retrieve()
                .body(Object[].class);

        assertThat(response).isNotNull();
    }

    @Test
    @Order(6)
    @DisplayName("POST /inventario/camisa - Crear camisa con token")
    void shouldCreateCamisa() {
        assertThat(authToken).isNotNull();

        Map<String, Object> camisaRequest = Map.of(
                "marca", "Nike",
                "talla", "M",
                "precio", 59.99,
                "cantidad", 10,
                "tipoEstampado", "BORDADO"
        );

        RestClient clientWithAuth = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultHeader("Authorization", "Bearer " + authToken)
                .build();

        Map<String, Object> response = clientWithAuth.post()
                .uri("/inventario/camisa")
                .body(camisaRequest)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();
        assertThat(response.get("marca")).isEqualTo("Nike");
    }

    @Test
    @Order(7)
    @DisplayName("POST /inventario/pantalon - Crear pantalón con token")
    void shouldCreatePantalon() {
        assertThat(authToken).isNotNull();

        Map<String, Object> pantalonRequest = Map.of(
                "marca", "Adidas",
                "talla", "L",
                "precio", 45.50,
                "cantidad", 20
        );

        RestClient clientWithAuth = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultHeader("Authorization", "Bearer " + authToken)
                .build();

        Map<String, Object> response = clientWithAuth.post()
                .uri("/inventario/pantalon")
                .body(pantalonRequest)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        assertThat(response).isNotNull();
        assertThat(response.get("id")).isNotNull();
        assertThat(response.get("marca")).isEqualTo("Adidas");
    }

    @Test
    @Order(8)
    @DisplayName("PATCH /inventario/{id}/stock - Actualizar stock (usando PATCH)")
    void shouldUpdateStock() {
        assertThat(authToken).isNotNull();

        // Primero crear una prenda
        Map<String, Object> pantalonRequest = Map.of(
                "marca", "Puma",
                "talla", "S",
                "precio", 30.0,
                "cantidad", 5
        );

        RestClient clientWithAuth = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultHeader("Authorization", "Bearer " + authToken)
                .build();

        Map<String, Object> createdPantalon = clientWithAuth.post()
                .uri("/inventario/pantalon")
                .body(pantalonRequest)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        assertThat(createdPantalon).isNotNull();
        Integer id = (Integer) createdPantalon.get("id");
        assertThat(id).isNotNull();

        // Actualizar stock usando PATCH (no PUT)
        Map<String, Object> updatedPantalon = clientWithAuth.patch()  // ← Cambiado de put() a patch()
                .uri("/inventario/" + id + "/stock?cantidad=15")  // ← Parámetro como query param
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        assertThat(updatedPantalon).isNotNull();
        assertThat(updatedPantalon.get("cantidad")).isEqualTo(15);
    }

    // ========================================================================
    // Pruebas de Ventas (Requieren Token)
    // ========================================================================

    @Test
    @Order(9)
    @DisplayName("POST /ventas/{prendaId}?cantidad=X - Realizar venta")
    void shouldSellProduct() {
        assertThat(authToken).isNotNull();

        // Primero crear prenda con stock
        Map<String, Object> pantalonRequest = Map.of(
                "marca", "Reebok",
                "talla", "XL",
                "precio", 75.0,
                "cantidad", 10
        );

        RestClient clientWithAuth = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultHeader("Authorization", "Bearer " + authToken)
                .build();

        Map<String, Object> createdPantalon = clientWithAuth.post()
                .uri("/inventario/pantalon")
                .body(pantalonRequest)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        assertThat(createdPantalon).isNotNull();
        Integer id = (Integer) createdPantalon.get("id");

        // Realizar venta - usando query param, no body
        Map<String, Object> ventaResponse = clientWithAuth.post()
                .uri("/ventas/" + id + "?cantidad=2")  // ← Parámetro como query param
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        assertThat(ventaResponse).isNotNull();
        assertThat(ventaResponse.get("prendaId")).isEqualTo(id);

        Object precioVenta = ventaResponse.get("precioVenta");
        if (precioVenta instanceof Number) {
            assertThat(((Number) precioVenta).doubleValue()).isEqualTo(150.0);
        }
    }
}