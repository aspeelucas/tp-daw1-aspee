package com.taller.tallermecanico.repository;

import com.taller.tallermecanico.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
