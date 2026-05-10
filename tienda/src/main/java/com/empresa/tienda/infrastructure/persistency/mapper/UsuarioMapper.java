package com.empresa.tienda.infrastructure.persistency.mapper;

import com.empresa.tienda.domain.model.Usuario;
import com.empresa.tienda.infrastructure.persistency.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) return null;

        Usuario usuario = new Usuario();
        usuario.setId(entity.getId());
        usuario.setUsername(entity.getUsername());
        usuario.setPassword(entity.getPassword());
        usuario.setNombre(entity.getNombre());
        usuario.setLastActive(entity.getLastActive());

        return usuario;
    }

    public UsuarioEntity toEntity(Usuario domain) {
        if (domain == null) return null;

        UsuarioEntity entity = new UsuarioEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setUsername(domain.getUsername());
        entity.setPassword(domain.getPassword());
        entity.setNombre(domain.getNombre());
        entity.setLastActive(domain.getLastActive());

        return entity;
    }
}