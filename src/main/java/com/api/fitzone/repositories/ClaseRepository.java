package com.api.fitzone.repositories;

import com.api.fitzone.models.Clase;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Integer> {

    // Bloquea la fila de la clase hasta terminar la transacción (evita sobrecupo)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Clase c WHERE c.idClase = :id")
    Optional<Clase> findByIdForUpdate(@Param("id") Integer id);

    List<Clase> findByRecordatorioEnviadoFalseAndFechaHoraBetween(LocalDateTime desde, LocalDateTime hasta);
}
