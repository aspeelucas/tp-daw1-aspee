
package com.taller.tallermecanico.controller;

import com.taller.tallermecanico.dto.OrdenDTO;
import com.taller.tallermecanico.model.EstadoOrden;
import com.taller.tallermecanico.model.Orden;
import com.taller.tallermecanico.service.OrdenService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    private OrdenDTO convertirADTO(Orden orden) {

        OrdenDTO dto = new OrdenDTO();

        dto.setId(orden.getId());
        dto.setVehiculoId(orden.getVehiculo().getId());
        dto.setDescripcion(orden.getDescripcion());
        dto.setEstado(orden.getEstado());
        dto.setCostoFinal(orden.getCostoFinal());

        return dto;
    }

    @GetMapping
    public List<OrdenDTO> listar(
            @RequestParam(required = false) EstadoOrden estado) {

        return ordenService.listar(estado)
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @GetMapping("/{id}")
    public OrdenDTO buscarPorId(@PathVariable Long id) {

        return convertirADTO(ordenService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<OrdenDTO> crear(
            @Valid @RequestBody OrdenDTO datos) {

        Orden creada = ordenService.crear(
                datos.getVehiculoId(),
                datos.getDescripcion());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(convertirADTO(creada));
    }

    public record ActualizacionOrden(
            String descripcion,
            BigDecimal costoFinal) {
    }

    @PutMapping("/{id}")
    public OrdenDTO actualizar(
            @PathVariable Long id,
            @RequestBody ActualizacionOrden datos) {

        Orden actualizada = ordenService.actualizar(
                id,
                datos.descripcion(),
                datos.costoFinal());

        return convertirADTO(actualizada);
    }

    public record CambioEstado(
            @NotNull(message = "El estado es obligatorio") EstadoOrden estado) {
    }

    @PatchMapping("/{id}/estado")
    public OrdenDTO cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstado datos) {

        Orden actualizada = ordenService.cambiarEstado(
                id, datos.estado());

        return convertirADTO(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        ordenService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
