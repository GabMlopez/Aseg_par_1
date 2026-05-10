package com.empresa.tienda.application.services;

import com.empresa.tienda.domain.model.Prenda;
import com.empresa.tienda.domain.ports.PrendaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioService {
    private final PrendaRepositoryPort prendaRepository;

    public List<Prenda> listarPrendas(){
        return prendaRepository.findAll();
    }

    public Prenda crearPrenda(Prenda prenda){
        return prendaRepository.save(prenda);
    }

    public Prenda actualizarStock(Long id, Integer cantidad){
        return prendaRepository.updateStock(id,cantidad);
    }

    public void eliminarPrenda(Long id){
        prendaRepository.deleteById(id);
    }
}
