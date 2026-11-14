
package com.taller.tallermecanico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Table(name = "ordenes")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @NotBlank(message = "La descripcion es obligatoria")
    @Column(nullable = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado = EstadoOrden.INGRESADO;

    private BigDecimal costoFinal;

    private LocalDateTime fechaIngreso;

    private LocalDateTime fechaInicioReparacion;

    private LocalDateTime fechaTerminacion;

    private LocalDateTime fechaEntrega;

    private LocalDateTime fechaCancelacion;

    public Orden() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public BigDecimal getCostoFinal() {
        return costoFinal;
    }

    public void setCostoFinal(BigDecimal costoFinal) {
        this.costoFinal = costoFinal;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDateTime getFechaInicioReparacion() {
        return fechaInicioReparacion;
    }

    public void setFechaInicioReparacion(LocalDateTime fechaInicioReparacion) {
        this.fechaInicioReparacion = fechaInicioReparacion;
    }

    public LocalDateTime getFechaTerminacion() {
        return fechaTerminacion;
    }

    public void setFechaTerminacion(LocalDateTime fechaTerminacion) {
        this.fechaTerminacion = fechaTerminacion;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public LocalDateTime getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(LocalDateTime fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    @Transient
    public Double getHorasEnReparacion() {

        if (fechaInicioReparacion == null
                || fechaTerminacion == null) {
            return null;
        }

        Duration duracion = Duration.between(
                fechaInicioReparacion,
                fechaTerminacion);

        return duracion.toSeconds() / 3600.0;
    }

}
