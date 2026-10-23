package com.api.fitzone.services;

import com.api.fitzone.models.Clase;
import com.api.fitzone.models.ClaseSocio;
import com.api.fitzone.models.Socio;
import com.api.fitzone.repositories.ClaseRepository;
import com.api.fitzone.repositories.ClaseSocioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecordatorioService {

    private static final Logger log = LoggerFactory.getLogger(RecordatorioService.class);

    private final ClaseRepository claseRepository;
    private final ClaseSocioRepository claseSocioRepository;

    public RecordatorioService(ClaseRepository claseRepository, ClaseSocioRepository claseSocioRepository) {
        this.claseRepository = claseRepository;
        this.claseSocioRepository = claseSocioRepository;
    }

    // Se ejecuta cada 60 segundos mientras la app esté corriendo
    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void enviarRecordatorios() {
        LocalDateTime ahora = LocalDateTime.now();

        // Clases que empiezan dentro de las próximas 2 horas y sin recordatorio enviado
        List<Clase> clases = claseRepository
                .findByRecordatorioEnviadoFalseAndFechaHoraBetween(ahora, ahora.plusHours(2));

        for (Clase clase : clases) {
            List<ClaseSocio> inscripciones = claseSocioRepository.findByClase_IdClase(clase.getIdClase());

            for (ClaseSocio inscripcion : inscripciones) {
                Socio socio = inscripcion.getSocio();
                log.info("[RECORDATORIO] Para {} <{}> / tel {}: tu clase de {} en {} es hoy a las {}",
                        socio.getNombre() + " " + socio.getApellido(), socio.getEmail(),
                        socio.getTelefono(), clase.getTipo(), clase.getSucursal(),
                        clase.getFechaHora().toLocalTime());
            }

            clase.setRecordatorioEnviado(true);
            claseRepository.save(clase);
        }
    }
}
