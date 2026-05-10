package com.empresa.tienda.infrastructure.persistency.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prendas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrendaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String tamanio;
    private Double precio;
    private Integer cantidad;

    @Column(name = "tipo_prenda")
    private String tipoPrenda;

}