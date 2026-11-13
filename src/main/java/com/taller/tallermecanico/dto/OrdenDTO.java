
package com.taller.tallermecanico.dto;

import com.taller.tallermecanico.model.EstadoOrden;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class OrdenDTO {

    private Long id;

    @NotNull(message = "El vehiculoId es obligatorio")
    private Long vehiculoId;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    private EstadoOrden estado;

    private BigDecimal costoFinal;

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
}
