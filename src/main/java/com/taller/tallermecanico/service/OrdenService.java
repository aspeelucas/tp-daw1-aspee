
package com.taller.tallermecanico.service;

import com.taller.tallermecanico.exception.ConflictoException;
import com.taller.tallermecanico.exception.RecursoNoEncontradoException;
import com.taller.tallermecanico.model.EstadoOrden;
import com.taller.tallermecanico.model.Orden;
import com.taller.tallermecanico.model.Vehiculo;
import com.taller.tallermecanico.repository.OrdenRepository;
import com.taller.tallermecanico.repository.VehiculoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final VehiculoRepository vehiculoRepository;

    public OrdenService(
            OrdenRepository ordenRepository,
            VehiculoRepository vehiculoRepository) {
        this.ordenRepository = ordenRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<Orden> listar(EstadoOrden estado) {
        if (estado == null) {
            return ordenRepository.findAll();
        }
        return ordenRepository.findByEstado(estado);
    }

    public Orden buscarPorId(Long id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una orden con ID " + id));
    }

    @Transactional
    public Orden crear(Long vehiculoId, String descripcion) {

        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un vehiculo con ID " + vehiculoId));

        boolean tieneOrdenAbierta = ordenRepository.existsByVehiculoIdAndEstadoNotIn(
                vehiculoId,
                List.of(
                        EstadoOrden.ENTREGADO,
                        EstadoOrden.CANCELADO));

        if (tieneOrdenAbierta) {
            throw new ConflictoException(
                    "El vehiculo ya tiene una orden abierta");
        }

        Orden orden = new Orden();
        orden.setVehiculo(vehiculo);
        orden.setDescripcion(descripcion);
        orden.setEstado(EstadoOrden.INGRESADO);

        return ordenRepository.save(orden);
    }

    @Transactional
    public Orden actualizar(
            Long id,
            String descripcion,
            BigDecimal costoFinal) {

        Orden orden = buscarPorId(id);

        if (descripcion != null) {
            orden.setDescripcion(descripcion);
        }

        if (costoFinal != null) {
            orden.setCostoFinal(costoFinal);
        }

        return ordenRepository.save(orden);
    }

    @Transactional
    public Orden cambiarEstado(Long id, EstadoOrden nuevoEstado) {

        Orden orden = buscarPorId(id);
        EstadoOrden actual = orden.getEstado();

        boolean permitido = switch (actual) {
            case INGRESADO ->
                nuevoEstado == EstadoOrden.EN_REPARACION
                        || nuevoEstado == EstadoOrden.CANCELADO;

            case EN_REPARACION ->
                nuevoEstado == EstadoOrden.TERMINADO;

            case TERMINADO ->
                nuevoEstado == EstadoOrden.ENTREGADO;

            case ENTREGADO, CANCELADO -> false;
        };

        if (!permitido) {
            throw new ConflictoException(
                    "Transicion de estado no permitida");
        }

        if (nuevoEstado == EstadoOrden.ENTREGADO
                && orden.getCostoFinal() == null) {
            throw new ConflictoException(
                    "No se puede entregar una orden sin costo final");
        }

        orden.setEstado(nuevoEstado);

        return ordenRepository.save(orden);
    }

    @Transactional
    public void eliminar(Long id) {
        Orden orden = buscarPorId(id);
        ordenRepository.delete(orden);
    }
}
