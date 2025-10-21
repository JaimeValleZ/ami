package com.ami.api.domain.paciente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Page<Paciente> findAllByActivoTrue(Pageable paginacion);

    @Query("""
        SELECT p.activo
                FROM Paciente p
                        WHERE p.id = :id
        """)
    boolean findActivoById(Long id);

    Paciente findByUsuarioEmail(String email);
}
