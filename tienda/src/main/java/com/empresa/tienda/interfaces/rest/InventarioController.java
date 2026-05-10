package com.empresa.tienda.interfaces.rest;

import com.empresa.tienda.application.services.InventarioService;
import com.empresa.tienda.domain.model.Camisa;
import com.empresa.tienda.domain.model.Pantalon;
import com.empresa.tienda.domain.model.Prenda;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario")
@RequiredArgsConstructor
public class InventarioController {
    private final InventarioService inventarioService;

    @GetMapping
    public List<Prenda> listar(){
        return inventarioService.listarPrendas();
    }

    @PostMapping("/camisa")
    public Camisa crearCamisa(@RequestBody Camisa camisa) {
        return (Camisa) inventarioService.crearPrenda(camisa);
    }

    @PostMapping("/pantalon")
    public Pantalon crearPantalon(@RequestBody Pantalon pantalon) {
        return (Pantalon) inventarioService.crearPrenda(pantalon);
    }

    @PatchMapping("/{id}/stock")
    public Prenda actualizarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        return inventarioService.actualizarStock(id, cantidad);
    }
}
