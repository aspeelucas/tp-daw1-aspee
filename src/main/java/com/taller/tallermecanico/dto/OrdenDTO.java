
package com.taller.tallermecanico.dto;

import com.taller.tallermecanico.model.EstadoOrden;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrdenDTO {

    private Long id;

    @NotNull(message = "El vehiculoId es obligatorio")
    private Long vehiculoId;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    private EstadoOrden estado;

    private BigDecimal costoFinal;

    private BigDecimal costoRepuestos;

    private LocalDateTime fechaIngreso;

    private LocalDateTime fechaInicioReparacion;

    private LocalDateTime fechaTerminacion;

    private LocalDateTime fechaEntrega;

    private LocalDateTime fechaCancelacion;

    private Double horasEnReparacion;

    public OrdenDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(Long vehiculoId) {
        this.vehiculoId = vehiculoId;
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

    public Double getHorasEnReparacion() {
        return horasEnReparacion;
    }

    public void setHorasEnReparacion(Double horasEnReparacion) {
        this.horasEnReparacion = horasEnReparacion;
    }

    public BigDecimal getCostoRepuestos() {
        return costoRepuestos;
    }

    public void setCostoRepuestos(BigDecimal costoRepuestos) {
        this.costoRepuestos = costoRepuestos;
    }

}
