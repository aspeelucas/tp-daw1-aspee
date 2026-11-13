package com.taller.tallermecanico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class VehiculoDTO {

    private Long id;

    @NotBlank(message = "La patente es obligatoria")
    @Pattern(regexp = "([A-Z]{3}[0-9]{3}|[A-Z]{2}[0-9]{3}[A-Z]{2})", message = "La patente debe tener formato ABC123 o AB123CD")
    private String patente;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    public VehiculoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}
