package com.empresa.tienda.infrastructure.persistency.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs_acceso")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogAccesoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;

    private String actividad;
    private LocalDateTime fecha;
}