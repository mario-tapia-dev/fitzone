package com.api.fitzone.repositories;

import com.api.fitzone.models.ClaseSocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClaseSocioRepository extends JpaRepository<ClaseSocio, Integer> {

    boolean existsByClase_IdClaseAndSocio_IdSocio(Integer idClase, Integer idSocio);

    Optional<ClaseSocio> findByClase_IdClaseAndSocio_IdSocio(Integer idClase, Integer idSocio);

    List<ClaseSocio> findByClase_IdClase(Integer idClase);

    List<ClaseSocio> findBySocio_IdSocio(Integer idSocio);

    void deleteByClase_IdClase(Integer idClase);
}
