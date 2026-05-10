package com.empresa.tienda.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pantalon implements Prenda{
    private Long id;
    private String marca;
    private String tamanio;
    private Double precio;
    private Integer cantidad;

    @Override
    public String getTipo(){
        return "PANTALON";
    }
}
