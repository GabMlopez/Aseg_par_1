package com.empresa.tienda.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private Long id;
    private String username;
    private String password;
    private String nombre;
    private LocalDateTime lastActive;

    public void actualizarLastActive(){
        this.lastActive = LocalDateTime.now();
    }
}
