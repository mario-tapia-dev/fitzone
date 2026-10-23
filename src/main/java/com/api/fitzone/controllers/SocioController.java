package com.api.fitzone.controllers;

import com.api.fitzone.models.ClaseSocio;
import com.api.fitzone.models.Socio;
import com.api.fitzone.services.InscripcionService;
import com.api.fitzone.services.SocioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socios")
public class SocioController {

    private final SocioService socioService;
    private final InscripcionService inscripcionService;

    public SocioController(SocioService socioService, InscripcionService inscripcionService) {
        this.socioService = socioService;
        this.inscripcionService = inscripcionService;
    }

    @GetMapping
    public List<Socio> listar() {
        return socioService.listar();
    }

    @GetMapping("/{idSocio}")
    public Socio obtener(@PathVariable Integer idSocio) {
        return socioService.obtener(idSocio);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Socio crear(@RequestBody Socio socio) {
        return socioService.crear(socio);
    }

    @PutMapping("/{idSocio}")
    public Socio actualizar(@PathVariable Integer idSocio, @RequestBody Socio socio) {
        return socioService.actualizar(idSocio, socio);
    }

    @DeleteMapping("/{idSocio}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer idSocio) {
        socioService.eliminar(idSocio);
    }

    // Clases en las que está inscrito un socio
    @GetMapping("/{idSocio}/clases")
    public List<ClaseSocio> clasesDelSocio(@PathVariable Integer idSocio) {
        return inscripcionService.listarPorSocio(idSocio);
    }
}
