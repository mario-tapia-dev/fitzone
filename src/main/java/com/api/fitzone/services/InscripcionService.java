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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InscripcionService {

    private static final Logger log = LoggerFactory.getLogger(InscripcionService.class);

    private final ClaseSocioRepository claseSocioRepository;
    private final ClaseRepository claseRepository;
    private final SocioRepository socioRepository;
    private final MembresiaRepository membresiaRepository;

    public InscripcionService(ClaseSocioRepository claseSocioRepository,
                              ClaseRepository claseRepository,
                              SocioRepository socioRepository,
                              MembresiaRepository membresiaRepository) {
        this.claseSocioRepository = claseSocioRepository;
        this.claseRepository = claseRepository;
        this.socioRepository = socioRepository;
        this.membresiaRepository = membresiaRepository;
    }

    @Transactional
    public ClaseSocio inscribir(Integer idClase, Integer idSocio) {
        // 1. El socio elige una clase (ambos deben existir)
        Socio socio = socioRepository.findById(idSocio)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Socio no encontrado: " + idSocio));

        // Se bloquea la clase hasta terminar la transacción para evitar sobrecupo
        Clase clase = claseRepository.findByIdForUpdate(idClase)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Clase no encontrada: " + idClase));

        if (clase.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La clase ya pasó");
        }

        // 2. Verificar que la membresía esté vigente (a la fecha de la clase)
        Membresia membresia = membresiaRepository.findBySocio_IdSocio(idSocio)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "El socio " + idSocio + " no tiene membresía"));
        if (membresia.getCaducidad().isBefore(clase.getFechaHora().toLocalDate())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Membresía vencida o por vencer antes de la clase (caduca el "
                            + membresia.getCaducidad() + ")");
        }

        // Evitar doble inscripción
        if (claseSocioRepository.existsByClase_IdClaseAndSocio_IdSocio(idClase, idSocio)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El socio ya está inscrito en esta clase");
        }

        // 3. Revisar que haya cupo
        if (clase.getRegistrados() >= clase.getMaximo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La clase está llena (" + clase.getMaximo() + "/" + clase.getMaximo() + ")");
        }

        // 4. Registrar la inscripción
        ClaseSocio inscripcion = new ClaseSocio();
        inscripcion.setClase(clase);
        inscripcion.setSocio(socio);
        ClaseSocio guardada = claseSocioRepository.save(inscripcion);

        clase.setRegistrados(clase.getRegistrados() + 1);
        claseRepository.save(clase);

        // 5. Enviar confirmación (simulada en terminal)
        log.info("[CONFIRMACION] Para {} <{}> / tel {}: inscrito a {} en {} el {} ({}/{} lugares)",
                socio.getNombre() + " " + socio.getApellido(), socio.getEmail(), socio.getTelefono(),
                clase.getTipo(), clase.getSucursal(), clase.getFechaHora(),
                clase.getRegistrados(), clase.getMaximo());

        return guardada;
    }

    @Transactional
    public void cancelar(Integer idClase, Integer idSocio) {
        ClaseSocio inscripcion = claseSocioRepository
                .findByClase_IdClaseAndSocio_IdSocio(idClase, idSocio)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "El socio " + idSocio + " no está inscrito en la clase " + idClase));

        Clase clase = claseRepository.findByIdForUpdate(idClase)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Clase no encontrada: " + idClase));

        // Borrar el registro de Clase_Socio y reducir en 1 los registrados
        claseSocioRepository.delete(inscripcion);
        clase.setRegistrados(Math.max(0, clase.getRegistrados() - 1));
        claseRepository.save(clase);

        log.info("[CANCELACION] Socio {} canceló su lugar en la clase {} ({}/{} lugares)",
                idSocio, idClase, clase.getRegistrados(), clase.getMaximo());
    }

    public List<ClaseSocio> listarPorClase(Integer idClase) {
        if (!claseRepository.existsById(idClase)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clase no encontrada: " + idClase);
        }
        return claseSocioRepository.findByClase_IdClase(idClase);
    }

    public List<ClaseSocio> listarPorSocio(Integer idSocio) {
        if (!socioRepository.existsById(idSocio)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Socio no encontrado: " + idSocio);
        }
        return claseSocioRepository.findBySocio_IdSocio(idSocio);
    }
}
