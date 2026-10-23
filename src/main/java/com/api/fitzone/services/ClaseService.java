package com.api.fitzone.services;

import com.api.fitzone.models.Clase;
import com.api.fitzone.repositories.ClaseRepository;
import com.api.fitzone.repositories.ClaseSocioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClaseService {

    private static final Logger log = LoggerFactory.getLogger(ClaseService.class);
    private static final int MAXIMO_POR_DEFECTO = 20;

    private final ClaseRepository claseRepository;
    private final ClaseSocioRepository claseSocioRepository;

    public ClaseService(ClaseRepository claseRepository, ClaseSocioRepository claseSocioRepository) {
        this.claseRepository = claseRepository;
        this.claseSocioRepository = claseSocioRepository;
    }

    public List<Clase> listar() {
        return claseRepository.findAll();
    }

    public Clase obtener(Integer idClase) {
        return claseRepository.findById(idClase)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Clase no encontrada: " + idClase));
    }

    @Transactional
    public Clase crear(Clase clase) {
        validarCampos(clase);
        clase.setIdClase(null);
        clase.setRegistrados(0);
        clase.setRecordatorioEnviado(false);
        if (clase.getMaximo() == null) {
            clase.setMaximo(MAXIMO_POR_DEFECTO);
        }
        validarMaximo(clase.getMaximo(), 0);

        Clase guardada = claseRepository.save(clase);
        log.info("[CLASE] Clase creada: {} en {} el {} (cupo {})", guardada.getTipo(),
                guardada.getSucursal(), guardada.getFechaHora(), guardada.getMaximo());
        return guardada;
    }

    @Transactional
    public Clase actualizar(Integer idClase, Clase datos) {
        Clase existente = claseRepository.findByIdForUpdate(idClase)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Clase no encontrada: " + idClase));
        validarCampos(datos);

        int nuevoMaximo = datos.getMaximo() != null ? datos.getMaximo() : existente.getMaximo();
        validarMaximo(nuevoMaximo, existente.getRegistrados());

        // Si cambia la fecha, el recordatorio debe poder volver a enviarse
        if (!existente.getFechaHora().equals(datos.getFechaHora())) {
            existente.setRecordatorioEnviado(false);
        }

        existente.setTipo(datos.getTipo());
        existente.setSucursal(datos.getSucursal());
        existente.setMaximo(nuevoMaximo);
        existente.setFechaHora(datos.getFechaHora());
        return claseRepository.save(existente);
    }

    @Transactional
    public void eliminar(Integer idClase) {
        Clase clase = obtener(idClase);
        claseSocioRepository.deleteByClase_IdClase(idClase);
        claseRepository.delete(clase);
        log.info("[CLASE] Clase {} eliminada junto con sus inscripciones", idClase);
    }

    private void validarCampos(Clase clase) {
        if (clase.getTipo() == null || clase.getSucursal() == null || clase.getFechaHora() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "tipo, sucursal y fechaHora son obligatorios");
        }
    }

    private void validarMaximo(int maximo, int registrados) {
        if (maximo < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El máximo debe ser al menos 1");
        }
        if (maximo < registrados) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede bajar el máximo a " + maximo + ": ya hay " + registrados + " inscritos");
        }
    }
}
