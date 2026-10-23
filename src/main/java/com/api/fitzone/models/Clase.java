package com.api.fitzone.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clases")
public class Clase {

    public enum Tipo {
        YOGA, SPINNING, FUNCIONAL
    }

    public enum Sucursal {
        SUCURSAL_1, SUCURSAL_2, SUCURSAL_3
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clase")
    private Integer idClase;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private Tipo tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "sucursal", nullable = false)
    private Sucursal sucursal;

    @Column(name = "maximo", nullable = false)
    private Integer maximo;

    @Column(name = "registrados", nullable = false)
    private Integer registrados;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "recordatorio_enviado", nullable = false)
    private Boolean recordatorioEnviado;

    public Clase() {
    }

    public Integer getIdClase() {
        return idClase;
    }

    public void setIdClase(Integer idClase) {
        this.idClase = idClase;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Integer getMaximo() {
        return maximo;
    }

    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
    }

    public Integer getRegistrados() {
        return registrados;
    }

    public void setRegistrados(Integer registrados) {
        this.registrados = registrados;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Boolean getRecordatorioEnviado() {
        return recordatorioEnviado;
    }

    public void setRecordatorioEnviado(Boolean recordatorioEnviado) {
        this.recordatorioEnviado = recordatorioEnviado;
    }
}
