package com.empresa.tienda.interfaces.rest;

import com.empresa.tienda.application.services.AuthService;
import com.empresa.tienda.domain.model.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public AuthResponse login(@Valid @RequestBody LoginRequest request){
        String token = authService.login(request.username(),request.password());
        return new AuthResponse(token);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario register(@Valid @RequestBody Usuario usuario) {
        return authService.register(usuario);
    }

    public record LoginRequest(String username, String password) {}
    public record AuthResponse(String token) {}
}
