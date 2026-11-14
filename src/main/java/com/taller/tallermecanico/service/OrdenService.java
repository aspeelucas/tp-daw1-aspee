
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
import com.taller.tallermecanico.repository.RepuestoUtilizadoRepository;
import java.time.LocalDateTime;
import com.taller.tallermecanico.exception.DatosInvalidosException;
import java.time.YearMonth;
import java.util.Map;
import java.util.List;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final VehiculoRepository vehiculoRepository;
    private final RepuestoUtilizadoRepository repuestoUtilizadoRepository;

    private BigDecimal calcularCostoRepuestosInterno(Long ordenId) {

        return repuestoUtilizadoRepository.findByOrdenId(ordenId)
                .stream()
                .map(uso -> uso.getSubtotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularCostoRepuestos(Long ordenId) {

        return calcularCostoRepuestosInterno(ordenId);
    }

    public OrdenService(
            OrdenRepository ordenRepository,
            VehiculoRepository vehiculoRepository,
            RepuestoUtilizadoRepository repuestoUtilizadoRepository) {

        this.ordenRepository = ordenRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.repuestoUtilizadoRepository = repuestoUtilizadoRepository;
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
        orden.setFechaIngreso(LocalDateTime.now());

        return ordenRepository.save(orden);
    }

    @Transactional
    public Orden actualizar(
            Long id,
            String descripcion,
            BigDecimal costoFinal) {

        Orden orden = buscarPorId(id);

        BigDecimal costoRepuestos = calcularCostoRepuestosInterno(id);

        if (costoFinal != null
                && costoFinal.compareTo(costoRepuestos) < 0) {

            throw new DatosInvalidosException(
                    "El costo final no puede ser inferior al costo de los repuestos");
        }

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

        LocalDateTime fechaCambio = LocalDateTime.now();

        switch (nuevoEstado) {
            case EN_REPARACION ->
                orden.setFechaInicioReparacion(fechaCambio);

            case TERMINADO ->
                orden.setFechaTerminacion(fechaCambio);

            case ENTREGADO ->
                orden.setFechaEntrega(fechaCambio);

            case CANCELADO ->
                orden.setFechaCancelacion(fechaCambio);

            case INGRESADO -> {
                // El ingreso se registra al crear la orden.
            }
        }

        orden.setEstado(nuevoEstado);

        return ordenRepository.save(orden);

    }

    @Transactional
    public void eliminar(Long id) {

        Orden orden = buscarPorId(id);

        boolean tieneRepuestosUtilizados = repuestoUtilizadoRepository.existsByOrdenId(id);

        if (tieneRepuestosUtilizados) {
            throw new ConflictoException(
                    "No se puede eliminar una orden que tiene repuestos utilizados");
        }

        ordenRepository.delete(orden);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerFacturacionMensual(YearMonth mes) {

        LocalDateTime inicio = mes.atDay(1).atStartOfDay();
        LocalDateTime fin = mes.plusMonths(1).atDay(1).atStartOfDay();

        List<Orden> ordenes = ordenRepository
                .buscarOrdenesPorEstadoYPeriodoDeEntrega(
                        EstadoOrden.ENTREGADO,
                        inicio,
                        fin);

        BigDecimal totalFacturado = ordenes.stream()
                .map(Orden::getCostoFinal)
                .filter(costo -> costo != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Map.of(
                "cantidadOrdenes", ordenes.size(),
                "totalFacturado", totalFacturado);
    }

}
