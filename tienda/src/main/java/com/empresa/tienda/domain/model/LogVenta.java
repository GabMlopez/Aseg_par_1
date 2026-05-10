package com.empresa.tienda.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogVenta {
    private Long id;
    private Long prendaId;
    private Double precioVenta;
    private LocalDateTime fechaGeneracion;
}
