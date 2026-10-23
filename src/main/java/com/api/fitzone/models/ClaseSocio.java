package com.api.fitzone.models;

import jakarta.persistence.*;

@Entity
@Table(name = "clase_socio", uniqueConstraints = @UniqueConstraint(columnNames = {"id_clase", "id_socio"}))
public class ClaseSocio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clase_socio")
    private Integer idClaseSocio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_clase", nullable = false)
    private Clase clase;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_socio", nullable = false)
    private Socio socio;

    public ClaseSocio() {
    }

    public Integer getIdClaseSocio() {
        return idClaseSocio;
    }

    public void setIdClaseSocio(Integer idClaseSocio) {
        this.idClaseSocio = idClaseSocio;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }
}
