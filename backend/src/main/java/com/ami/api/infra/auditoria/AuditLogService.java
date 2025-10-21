package com.ami.api.infra.auditoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository repository;

    public void registrarLog(String usuario, String accion, String detalle) {
        AuditLog log = AuditLog.builder()
                .usuario(usuario)
                .accion(accion)
                .detalle(detalle)
                .fechaHora(LocalDateTime.now())
                .build();

        repository.save(log);
    }
}
