package com.api.fitzone.controllers;

import com.api.fitzone.models.Clase;
import com.api.fitzone.models.ClaseSocio;
import com.api.fitzone.services.ClaseService;
import com.api.fitzone.services.InscripcionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clases")
public class ClaseController {

    private final ClaseService claseService;
    private final InscripcionService inscripcionService;

    public ClaseController(ClaseService claseService, InscripcionService inscripcionService) {
        this.claseService = claseService;
        this.inscripcionService = inscripcionService;
    }

    @GetMapping
    public List<Clase> listar() {
        return claseService.listar();
    }

    @GetMapping("/{idClase}")
    public Clase obtener(@PathVariable Integer idClase) {
        return claseService.obtener(idClase);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Clase crear(@RequestBody Clase clase) {
        return claseService.crear(clase);
    }

    @PutMapping("/{idClase}")
    public Clase actualizar(@PathVariable Integer idClase, @RequestBody Clase clase) {
        return claseService.actualizar(idClase, clase);
    }

    @DeleteMapping("/{idClase}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer idClase) {
        claseService.eliminar(idClase);
    }

    // ---- Inscripciones ----

    @GetMapping("/{idClase}/socios")
    public List<ClaseSocio> sociosInscritos(@PathVariable Integer idClase) {
        return inscripcionService.listarPorClase(idClase);
    }

    @PostMapping("/{idClase}/inscripciones/{idSocio}")
    @ResponseStatus(HttpStatus.CREATED)
    public ClaseSocio inscribir(@PathVariable Integer idClase, @PathVariable Integer idSocio) {
        return inscripcionService.inscribir(idClase, idSocio);
    }

    @DeleteMapping("/{idClase}/inscripciones/{idSocio}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable Integer idClase, @PathVariable Integer idSocio) {
        inscripcionService.cancelar(idClase, idSocio);
    }
}
