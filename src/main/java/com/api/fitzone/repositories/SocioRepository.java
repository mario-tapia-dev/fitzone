package com.api.fitzone.repositories;

import com.api.fitzone.models.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Integer> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdSocioNot(String email, Integer idSocio);
}
