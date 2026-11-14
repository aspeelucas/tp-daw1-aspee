
package com.taller.tallermecanico.repository;

import com.taller.tallermecanico.model.Orden;
import com.taller.tallermecanico.model.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

    List<Orden> findByEstado(EstadoOrden estado);

    List<Orden> findByVehiculoId(Long vehiculoId);

    boolean existsByVehiculoIdAndEstadoNotIn(
            Long vehiculoId,
            List<EstadoOrden> estados);

    java.util.List<Orden> findByVehiculoIdOrderByIdDesc(Long vehiculoId);

    @Query("""
            SELECT o FROM Orden o
            WHERE o.estado = :estado
              AND o.fechaEntrega >= :inicio
              AND o.fechaEntrega < :fin
            """)
    List<Orden> buscarOrdenesPorEstadoYPeriodoDeEntrega(
            @Param("estado") EstadoOrden estado,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

}
