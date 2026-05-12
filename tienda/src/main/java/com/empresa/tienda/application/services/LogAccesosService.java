package com.empresa.tienda.application.services;

import com.empresa.tienda.domain.model.LogAcceso;
import com.empresa.tienda.domain.ports.LogAccesoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogAccesosService {
    private final LogAccesoRepositoryPort logAccesoRepository;

    public LogAcceso createLog(LogAcceso acceso){
        return logAccesoRepository.save(acceso);
    }
    public List<LogAcceso> findByUsuarioId(Long usuario_id){
        return logAccesoRepository.findByUsuarioId(usuario_id);
    }

    public List<LogAcceso> findByDate(LocalDateTime date){
        return logAccesoRepository.findByDate(date);
    }
}
