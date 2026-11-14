

package com.taller.tallermecanico.repository;

import com.taller.tallermecanico.model.Repuesto;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RepuestoRepository
        extends JpaRepository<Repuesto, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Repuesto r WHERE r.id = :id")
    Optional<Repuesto> buscarPorIdParaActualizar(
            @Param("id") Long id);
}
