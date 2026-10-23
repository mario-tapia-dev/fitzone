package com.api.fitzone.controllers;

import com.api.fitzone.models.Membresia;
import com.api.fitzone.services.MembresiaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/membresias")
public class MembresiaController {

    private final MembresiaService membresiaService;

    public MembresiaController(MembresiaService membresiaService) {
        this.membresiaService = membresiaService;
    }

    @GetMapping
    public List<Membresia> listar() {
        return membresiaService.listar();
    }

    @GetMapping("/socio/{idSocio}")
    public Membresia obtenerPorSocio(@PathVariable Integer idSocio) {
        return membresiaService.obtenerPorSocio(idSocio);
    }

    // El socio paga un mes más
    @PutMapping("/socio/{idSocio}/extender")
    public Membresia extender(@PathVariable Integer idSocio) {
        return membresiaService.extender(idSocio);
    }
}
