package com.empresa.tienda.interfaces.rest;

import com.empresa.tienda.application.services.LogAccesosService;
import com.empresa.tienda.domain.model.LogAcceso;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogAccesoController {
    private final LogAccesosService logAccesosService;

    @GetMapping("/user/{user_id}")
    public List<LogAcceso> logsByUserId(@Valid @PathVariable Long user_id){
        return logAccesosService.findByUsuarioId(user_id);
    }

    @GetMapping("/date/{date}")
    public List<LogAcceso> logsByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return logAccesosService.findByDate(date);
    }

    @PostMapping("/create")
    public LogAcceso createLog(@Valid @RequestBody LogAcceso logAcceso){
        return logAccesosService.createLog(logAcceso);
    }
}
