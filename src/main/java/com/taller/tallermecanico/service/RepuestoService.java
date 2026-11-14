
package com.taller.tallermecanico.service;

import com.taller.tallermecanico.exception.RecursoNoEncontradoException;
import com.taller.tallermecanico.model.Repuesto;
import com.taller.tallermecanico.repository.RepuestoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RepuestoService {

    private final RepuestoRepository repuestoRepository;

    public RepuestoService(RepuestoRepository repuestoRepository) {
        this.repuestoRepository = repuestoRepository;
    }

    @Transactional(readOnly = true)
    public List<Repuesto> listar() {
        return repuestoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Repuesto buscarPorId(Long id) {
        return repuestoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un repuesto con ID " + id));
    }

    @Transactional
    public Repuesto crear(
            String nombre,
            BigDecimal precio,
            Integer stock) {

        Repuesto repuesto = new Repuesto();

        repuesto.setNombre(nombre);
        repuesto.setPrecio(precio);
        repuesto.setStock(stock);

        return repuestoRepository.save(repuesto);
    }
}
