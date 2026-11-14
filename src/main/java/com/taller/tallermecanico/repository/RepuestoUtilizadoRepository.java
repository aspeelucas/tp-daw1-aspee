
package com.taller.tallermecanico.repository;

import com.taller.tallermecanico.model.RepuestoUtilizado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepuestoUtilizadoRepository
        extends JpaRepository<RepuestoUtilizado, Long> {

    List<RepuestoUtilizado> findByOrdenId(Long ordenId);
    boolean existsByOrdenId(Long ordenId);
}
