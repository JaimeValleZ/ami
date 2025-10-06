package com.ami.api.infra.auditoria;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario;   // email o id del usuario que hizo la acción
    private String accion;    // CREATE, DELETE, AUTH, etc.
    private String detalle;   // descripción detallada
    private LocalDateTime fechaHora;
}
