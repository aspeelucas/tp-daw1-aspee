
package com.taller.tallermecanico.service;

import com.taller.tallermecanico.exception.ConflictoException;
import com.taller.tallermecanico.exception.RecursoNoEncontradoException;
import com.taller.tallermecanico.model.Orden;
import com.taller.tallermecanico.model.Repuesto;
import com.taller.tallermecanico.model.RepuestoUtilizado;
import com.taller.tallermecanico.repository.OrdenRepository;
import com.taller.tallermecanico.repository.RepuestoRepository;
import com.taller.tallermecanico.repository.RepuestoUtilizadoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RepuestoUtilizadoService {

    private final OrdenRepository ordenRepository;
    private final RepuestoRepository repuestoRepository;
    private final RepuestoUtilizadoRepository repuestoUtilizadoRepository;

    public RepuestoUtilizadoService(
            OrdenRepository ordenRepository,
            RepuestoRepository repuestoRepository,
            RepuestoUtilizadoRepository repuestoUtilizadoRepository) {

        this.ordenRepository = ordenRepository;
        this.repuestoRepository = repuestoRepository;
        this.repuestoUtilizadoRepository = repuestoUtilizadoRepository;
    }

    @Transactional
    public RepuestoUtilizado registrarUso(
            Long ordenId,
            Long repuestoId,
            Integer cantidad) {

        // Validar la cantidad solicitada
        if (cantidad == null || cantidad <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La cantidad debe ser mayor a cero");
        }

        // Verificar que exista la orden
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una orden con ID " + ordenId));

        // Buscar el repuesto con bloqueo de escritura
        Repuesto repuesto = repuestoRepository
                .buscarPorIdParaActualizar(repuestoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un repuesto con ID " + repuestoId));

        // Comprobar las existencias disponibles
        if (repuesto.getStock() < cantidad) {
            throw new ConflictoException(
                    "Stock insuficiente para el repuesto "
                            + repuesto.getNombre());
        }

        // Descontar las unidades utilizadas
        repuesto.setStock(repuesto.getStock() - cantidad);

        // Registrar el repuesto utilizado en la orden
        RepuestoUtilizado uso = new RepuestoUtilizado();
        uso.setOrden(orden);
        uso.setRepuesto(repuesto);
        uso.setCantidad(cantidad);

        // Conservar el precio del momento de utilización
        uso.setPrecioUnitario(repuesto.getPrecio());

        repuestoRepository.save(repuesto);

        return repuestoUtilizadoRepository.save(uso);
    }
}
