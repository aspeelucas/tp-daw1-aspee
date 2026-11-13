package com.taller.tallermecanico.service;

import com.taller.tallermecanico.exception.ConflictoException;
import com.taller.tallermecanico.exception.RecursoNoEncontradoException;
import com.taller.tallermecanico.model.Cliente;
import com.taller.tallermecanico.model.Vehiculo;
import com.taller.tallermecanico.repository.ClienteRepository;
import com.taller.tallermecanico.repository.VehiculoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;

    public VehiculoService(
            VehiculoRepository vehiculoRepository,
            ClienteRepository clienteRepository) {

        this.vehiculoRepository = vehiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Vehiculo> listar() {
        return vehiculoRepository.findAll();
    }

    public Vehiculo buscarPorId(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un vehiculo con ID " + id));
    }

    public List<Vehiculo> listarPorCliente(Long clienteId) {

        buscarCliente(clienteId);

        return vehiculoRepository.findByClienteId(clienteId);
    }

    private Cliente buscarCliente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un cliente con ID " + clienteId));
    }

    @Transactional
    public Vehiculo crear(Vehiculo vehiculo, Long clienteId) {

        Cliente cliente = buscarCliente(clienteId);

        if (vehiculoRepository.existsByPatente(vehiculo.getPatente())) {
            throw new ConflictoException(
                    "Ya existe un vehiculo con esa patente");
        }

        vehiculo.setId(null);
        vehiculo.setCliente(cliente);

        return vehiculoRepository.save(vehiculo);
    }

    @Transactional
    public Vehiculo actualizar(
            Long id, Vehiculo datos, Long clienteId) {

        Vehiculo vehiculo = buscarPorId(id);
        Cliente cliente = buscarCliente(clienteId);

        if (vehiculoRepository.existsByPatenteAndIdNot(
                datos.getPatente(), id)) {
            throw new ConflictoException(
                    "Ya existe un vehiculo con esa patente");
        }

        vehiculo.setPatente(datos.getPatente());
        vehiculo.setMarca(datos.getMarca());
        vehiculo.setModelo(datos.getModelo());
        vehiculo.setCliente(cliente);

        return vehiculoRepository.save(vehiculo);
    }

    @Transactional
    public void eliminar(Long id) {
        Vehiculo vehiculo = buscarPorId(id);
        vehiculoRepository.delete(vehiculo);
    }
}

