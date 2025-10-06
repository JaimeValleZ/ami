package com.ami.api.infra.security;

import com.ami.api.infra.auditoria.AuditLogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final AuditLogService auditLogService;

    public CustomAccessDeniedHandler(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // Obtener el usuario autenticado (si existe)
        String usuario = (request.getUserPrincipal() != null)
                ? request.getUserPrincipal().getName()
                : "ANONYMOUS";

        // Guardar en la tabla de logs
        auditLogService.registrarLog(
                usuario,
                "ACCESO_DENEGADO",
                "Intento fallido de acceder a: " + request.getRequestURI()
        );

        // Devolver el 403
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para acceder a este recurso");
    }
}
