package com.api.fitzone.services;

import com.api.fitzone.models.Membresia;
import com.api.fitzone.repositories.MembresiaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class MembresiaService {

    private static final Logger log = LoggerFactory.getLogger(MembresiaService.class);

    private final MembresiaRepository membresiaRepository;

    public MembresiaService(MembresiaRepository membresiaRepository) {
        this.membresiaRepository = membresiaRepository;
    }

    public List<Membresia> listar() {
        return membresiaRepository.findAll();
    }

    public Membresia obtenerPorSocio(Integer idSocio) {
        return membresiaRepository.findBySocio_IdSocio(idSocio)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No hay membresía para el socio " + idSocio));
    }

    // El socio "paga" un mes: se alarga la caducidad 1 mes
    @Transactional
    public Membresia extender(Integer idSocio) {
        Membresia membresia = obtenerPorSocio(idSocio);
        LocalDate hoy = LocalDate.now();
        LocalDate anterior = membresia.getCaducidad();

        // Si ya venció, el mes cuenta desde hoy; si sigue vigente, desde su caducidad
        LocalDate base = anterior.isBefore(hoy) ? hoy : anterior;
        membresia.setCaducidad(base.plusMonths(1));
        Membresia guardada = membresiaRepository.save(membresia);

        log.info("[MEMBRESIA] Pago recibido del socio {}. Caducidad: {} -> {}",
                idSocio, anterior, guardada.getCaducidad());
        return guardada;
    }
}
