package com.empresa.tienda.application.services;

import com.empresa.tienda.domain.model.Usuario;
import com.empresa.tienda.domain.ports.UserRepositoryPort;
import com.empresa.tienda.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public String login(String username,String password){
        Usuario user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no registrado"));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Contraseña incorrecta");
        }

        user.actualizarLastActive();
        userRepository.update(user);

        return tokenProvider.generarToken(username);
    }

    public Usuario register(Usuario user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.actualizarLastActive();
        return userRepository.save(user);
    }
}
