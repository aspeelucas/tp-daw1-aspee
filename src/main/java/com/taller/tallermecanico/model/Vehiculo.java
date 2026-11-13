
package com.taller.tallermecanico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "vehiculos", uniqueConstraints = @UniqueConstraint(columnNames = "patente"))
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La patente es obligatoria")
    @Pattern(regexp = "([A-Z]{3}[0-9]{3}|[A-Z]{2}[0-9]{3}[A-Z]{2})", message = "La patente debe tener formato ABC123 o AB123CD")
    @Column(nullable = false, unique = true)
    private String patente;

    @NotBlank(message = "La marca es obligatoria")
    @Column(nullable = false)
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Column(nullable = false)
    private String modelo;

    @NotNull(message = "El clienteId es obligatorio")
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    public Vehiculo() {
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
