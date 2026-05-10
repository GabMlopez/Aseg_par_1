package com.empresa.tienda.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Camisa implements Prenda {
    private Long id;
    private String marca;
    private String tamanio;
    private Double precio;
    private Integer cantidad;
    private TipoEstampado tipoEstampado;

    @Override
    public String getTipo() {
        if (tipoEstampado != null) {
            return "CAMISA_" + tipoEstampado.name();
        }
        return "CAMISA";
    }

    public enum TipoEstampado {
        PLASTICO, BORDADO
    }
}