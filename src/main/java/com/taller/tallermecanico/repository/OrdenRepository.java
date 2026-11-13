
package com.taller.tallermecanico.repository;

import com.taller.tallermecanico.model.Orden;
import com.taller.tallermecanico.model.EstadoOrden;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

    List<Orden> findByEstado(EstadoOrden estado);

    List<Orden> findByVehiculoId(Long vehiculoId);

    boolean existsByVehiculoIdAndEstadoNotIn(
            Long vehiculoId,
            List<EstadoOrden> estados);
}
