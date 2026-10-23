package com.api.fitzone.services;

import com.api.fitzone.models.Clase;
import com.api.fitzone.models.ClaseSocio;
import com.api.fitzone.models.Membresia;
import com.api.fitzone.models.Socio;
import com.api.fitzone.repositories.ClaseRepository;
import com.api.fitzone.repositories.ClaseSocioRepository;
import com.api.fitzone.repositories.MembresiaRepository;
import com.api.fitzone.repositories.SocioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class SocioService {

    private static final Logger log = LoggerFactory.getLogger(SocioService.class);

    private final SocioRepository socioRepository;
    private final MembresiaRepository membresiaRepository;
    private final ClaseSocioRepository claseSocioRepository;
    private final ClaseRepository claseRepository;

    public SocioService(SocioRepository socioRepository,
                        MembresiaRepository membresiaRepository,
                        ClaseSocioRepository claseSocioRepository,
                        ClaseRepository claseRepository) {
        this.socioRepository = socioRepository;
        this.membresiaRepository = membresiaRepository;
        this.claseSocioRepository = claseSocioRepository;
        this.claseRepository = claseRepository;
    }

    public List<Socio> listar() {
        return socioRepository.findAll();
    }

    public Socio obtener(Integer idSocio) {
        return socioRepository.findById(idSocio)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Socio no encontrado: " + idSocio));
    }

    @Transactional
    public Socio crear(Socio socio) {
        validarCampos(socio);
        if (socioRepository.existsByEmail(socio.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un socio con el email " + socio.getEmail());
        }

        socio.setIdSocio(null);
        Socio guardado = socioRepository.save(socio);

        // Al registrar un socio se crea su membresía (primer mes pagado)
        Membresia membresia = new Membresia();
        membresia.setSocio(guardado);
        membresia.setCaducidad(LocalDate.now().plusMonths(1));
        membresiaRepository.save(membresia);

        log.info("[SOCIO] Socio registrado: {} {} (id {}). Membresía vigente hasta {}",
                guardado.getNombre(), guardado.getApellido(), guardado.getIdSocio(),
                membresia.getCaducidad());
        return guardado;
    }

    @Transactional
    public Socio actualizar(Integer idSocio, Socio datos) {
        Socio existente = obtener(idSocio);
        validarCampos(datos);
        if (socioRepository.existsByEmailAndIdSocioNot(datos.getEmail(), idSocio)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe otro socio con el email " + datos.getEmail());
        }

        existente.setNombre(datos.getNombre());
        existente.setApellido(datos.getApellido());
        existente.setEmail(datos.getEmail());
        existente.setTelefono(datos.getTelefono());
        return socioRepository.save(existente);
    }

    @Transactional
    public void eliminar(Integer idSocio) {
        Socio socio = obtener(idSocio);

        // Liberar su lugar en todas las clases donde estaba inscrito
        for (ClaseSocio inscripcion : claseSocioRepository.findBySocio_IdSocio(idSocio)) {
            Clase clase = claseRepository.findByIdForUpdate(inscripcion.getClase().getIdClase())
                    .orElseThrow();
            clase.setRegistrados(Math.max(0, clase.getRegistrados() - 1));
            claseRepository.save(clase);
            claseSocioRepository.delete(inscripcion);
        }

        membresiaRepository.deleteBySocio_IdSocio(idSocio);
        socioRepository.delete(socio);
        log.info("[SOCIO] Socio {} eliminado junto con su membresía e inscripciones", idSocio);
    }

    private void validarCampos(Socio socio) {
        if (socio.getNombre() == null || socio.getNombre().isBlank()
                || socio.getApellido() == null || socio.getApellido().isBlank()
                || socio.getEmail() == null || socio.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "nombre, apellido y email son obligatorios");
        }
    }
}
