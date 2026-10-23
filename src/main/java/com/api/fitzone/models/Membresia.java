package com.api.fitzone.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "membresias")
public class Membresia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_membresia")
    private Integer idMembresia;

    @Column(name = "caducidad", nullable = false)
    private LocalDate caducidad;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_socio", nullable = false, unique = true)
    private Socio socio;

    public Membresia() {
    }

    public Integer getIdMembresia() {
        return idMembresia;
    }

    public void setIdMembresia(Integer idMembresia) {
        this.idMembresia = idMembresia;
    }

    public LocalDate getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(LocalDate caducidad) {
        this.caducidad = caducidad;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }
}
