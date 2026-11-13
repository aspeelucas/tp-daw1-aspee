package com.taller.tallermecanico.repository;

import com.taller.tallermecanico.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    boolean existsByPatente(String patente);

    boolean existsByPatenteAndIdNot(String patente, Long id);

    List<Vehiculo> findByClienteId(Long clienteId);
}
