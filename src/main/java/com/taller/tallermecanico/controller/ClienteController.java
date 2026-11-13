
package com.taller.tallermecanico.controller;

import com.taller.tallermecanico.model.Cliente;
import com.taller.tallermecanico.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.taller.tallermecanico.exception.RecursoNoEncontradoException;
import com.taller.tallermecanico.dto.VehiculoDTO;
import com.taller.tallermecanico.service.VehiculoService;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final VehiculoService vehiculoService;

    public ClienteController(
            ClienteService clienteService,
            VehiculoService vehiculoService) {

        this.clienteService = clienteService;
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {

        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un cliente con ID " + id));

        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@Valid @RequestBody Cliente cliente) {
        cliente.setId(null);
        Cliente creado = clienteService.guardar(cliente);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity.created(location).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Cliente datos) {

        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un cliente con ID " + id));

        cliente.setNombre(datos.getNombre());
        cliente.setTelefono(datos.getTelefono());

        return ResponseEntity.ok(clienteService.guardar(cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un cliente con ID " + id));

        clienteService.eliminar(cliente.getId());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/vehiculos")
    public List<VehiculoDTO> listarVehiculosDeCliente(
            @PathVariable Long id) {

        return vehiculoService.listarPorCliente(id)
                .stream()
                .map(vehiculo -> {
                    VehiculoDTO dto = new VehiculoDTO();

                    dto.setId(vehiculo.getId());
                    dto.setPatente(vehiculo.getPatente());
                    dto.setMarca(vehiculo.getMarca());
                    dto.setModelo(vehiculo.getModelo());
                    dto.setClienteId(vehiculo.getCliente().getId());

                    return dto;
                })
                .toList();
    }

}
