package com.empresa.tienda.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogAcceso {
    private Long id;
    private Long usuarioId;
    private String actividad;
    private LocalDateTime fecha;
}
