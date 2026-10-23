package com.api.fitzone.repositories;

import com.api.fitzone.models.Membresia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembresiaRepository extends JpaRepository<Membresia, Integer> {

    Optional<Membresia> findBySocio_IdSocio(Integer idSocio);

    void deleteBySocio_IdSocio(Integer idSocio);
}
