package com.empresa.tienda.infrastructure.persistency.adapter;

import com.empresa.tienda.domain.model.Usuario;
import com.empresa.tienda.domain.ports.UserRepositoryPort;
import com.empresa.tienda.infrastructure.persistency.entity.UsuarioEntity;
import com.empresa.tienda.infrastructure.persistency.mapper.UsuarioMapper;
import com.empresa.tienda.infrastructure.persistency.repository.JpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUsuarioRepository jpaRepository;
    private final UsuarioMapper mapper;

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
                .map(mapper::toDomain);
    }

    @Override
    public Usuario save(Usuario user) {
        UsuarioEntity entity = mapper.toEntity(user);
        UsuarioEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Usuario update(Usuario user) {
        UsuarioEntity entity = mapper.toEntity(user);
        UsuarioEntity updated = jpaRepository.save(entity);
        return mapper.toDomain(updated);
    }
}