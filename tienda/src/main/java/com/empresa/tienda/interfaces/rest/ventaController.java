package com.empresa.tienda.interfaces.rest;

import com.empresa.tienda.application.services.VentaService;
import com.empresa.tienda.domain.model.LogVenta;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ventas")
@RequiredArgsConstructor
public class ventaController {
    private final VentaService ventaService;

    @PostMapping("/{prendaid}")
    public LogVenta vender(@PathVariable Long prendaId, @RequestParam Integer cantidad){
        return ventaService.realizarVenta(prendaId,cantidad);
    }
}
