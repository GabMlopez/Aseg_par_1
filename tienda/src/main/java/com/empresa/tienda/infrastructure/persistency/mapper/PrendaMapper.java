package com.empresa.tienda.infrastructure.persistency.mapper;

import com.empresa.tienda.domain.model.Camisa;
import com.empresa.tienda.domain.model.Pantalon;
import com.empresa.tienda.domain.model.Prenda;
import com.empresa.tienda.infrastructure.persistency.entity.PrendaEntity;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
public class PrendaMapper {

    public Prenda toDomain(PrendaEntity entity) {
        if (entity == null) return null;

        String tipo = entity.getTipoPrenda();

        if (tipo == null) return null;

        if (tipo.startsWith("CAMISA_") && (tipo.contains("PLASTICO") || tipo.contains("BORDADO"))) {
            Camisa camisa = new Camisa();
            camisa.setId(entity.getId());
            camisa.setMarca(entity.getMarca());
            camisa.setTamanio(entity.getTamanio());
            camisa.setPrecio(entity.getPrecio());
            camisa.setCantidad(entity.getCantidad());

            if (tipo.contains("PLASTICO")) {
                camisa.setTipoEstampado(Camisa.TipoEstampado.PLASTICO);
            } else if (tipo.contains("BORDADO")) {
                camisa.setTipoEstampado(Camisa.TipoEstampado.BORDADO);
            }
            return camisa;
        }

        if ("CAMISA".equals(tipo)) {
            Camisa camisa = new Camisa();
            camisa.setId(entity.getId());
            camisa.setMarca(entity.getMarca());
            camisa.setTamanio(entity.getTamanio());
            camisa.setPrecio(entity.getPrecio());
            camisa.setCantidad(entity.getCantidad());
            camisa.setTipoEstampado(null);
            return camisa;
        }

        if ("PANTALON".equals(tipo)) {
            Pantalon pantalon = new Pantalon();
            pantalon.setId(entity.getId());
            pantalon.setMarca(entity.getMarca());
            pantalon.setTamanio(entity.getTamanio());
            pantalon.setPrecio(entity.getPrecio());
            pantalon.setCantidad(entity.getCantidad());
            return pantalon;
        }

        return null;
    }

    public PrendaEntity toEntity(Prenda domain) {
        if (domain == null) return null;

        PrendaEntity entity = new PrendaEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setMarca(domain.getMarca());
        entity.setTamanio(domain.getTamanio());
        entity.setPrecio(domain.getPrecio());
        entity.setCantidad(domain.getCantidad());
        entity.setTipoPrenda(domain.getTipo());

        return entity;
    }
}