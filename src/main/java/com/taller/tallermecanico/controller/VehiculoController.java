
package com.taller.tallermecanico.controller;

import com.taller.tallermecanico.dto.VehiculoDTO;
import com.taller.tallermecanico.model.Vehiculo;
import com.taller.tallermecanico.service.VehiculoService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    private VehiculoDTO convertirADTO(Vehiculo vehiculo) {

        VehiculoDTO dto = new VehiculoDTO();

        dto.setId(vehiculo.getId());
        dto.setPatente(vehiculo.getPatente());
        dto.setMarca(vehiculo.getMarca());
        dto.setModelo(vehiculo.getModelo());
        dto.setClienteId(vehiculo.getCliente().getId());

        return dto;
    }

    private Vehiculo convertirAEntidad(VehiculoDTO dto) {

        Vehiculo vehiculo = new Vehiculo();

        vehiculo.setPatente(dto.getPatente());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());

        return vehiculo;
    }

    @GetMapping
    public List<VehiculoDTO> listar() {

        return vehiculoService.listar()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @GetMapping("/{id}")
    public VehiculoDTO buscarPorId(@PathVariable Long id) {

        return convertirADTO(vehiculoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<VehiculoDTO> crear(
            @Valid @RequestBody VehiculoDTO datos) {

        Vehiculo vehiculo = convertirAEntidad(datos);

        Vehiculo creado = vehiculoService.crear(
                vehiculo, datos.getClienteId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(convertirADTO(creado));
    }

    @PutMapping("/{id}")
    public VehiculoDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody VehiculoDTO datos) {

        Vehiculo vehiculo = convertirAEntidad(datos);

        Vehiculo actualizado = vehiculoService.actualizar(
                id, vehiculo, datos.getClienteId());

        return convertirADTO(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        vehiculoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
