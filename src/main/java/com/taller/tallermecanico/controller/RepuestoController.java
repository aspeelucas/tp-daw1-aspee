
package com.taller.tallermecanico.controller;

import com.taller.tallermecanico.model.Repuesto;
import com.taller.tallermecanico.service.RepuestoService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/repuestos")
public class RepuestoController {

    private final RepuestoService repuestoService;

    public RepuestoController(RepuestoService repuestoService) {
        this.repuestoService = repuestoService;
    }

    public record DatosRepuesto(
            @NotBlank(message = "El nombre es obligatorio") String nombre,

            @NotNull(message = "El precio es obligatorio") @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a cero") BigDecimal precio,

            @NotNull(message = "El stock es obligatorio") @Min(value = 0, message = "El stock no puede ser negativo") Integer stock) {
    }

    @GetMapping
    public List<Repuesto> listar() {
        return repuestoService.listar();
    }

    @GetMapping("/{id}")
    public Repuesto buscarPorId(@PathVariable Long id) {
        return repuestoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Repuesto> crear(
            @Valid @RequestBody DatosRepuesto datos) {

        Repuesto creado = repuestoService.crear(
                datos.nombre(),
                datos.precio(),
                datos.stock());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(creado);
    }
}
