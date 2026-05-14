package com.empresa.tienda.infrastructure.persistency.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs_ventas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogVentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prenda_id")
    private Long prendaId;

    @Column(name = "precio_venta")
    private Double precioVenta;

    @Column(name = "fecha_generacion")
    private LocalDateTime fechaGeneracion;

}