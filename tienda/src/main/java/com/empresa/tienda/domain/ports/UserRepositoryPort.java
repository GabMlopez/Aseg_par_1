package com.empresa.tienda.domain.ports;

import com.empresa.tienda.domain.model.Usuario;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<Usuario> findByUsername(String user);
    Usuario save(Usuario user);
    Usuario update(Usuario user);
}
