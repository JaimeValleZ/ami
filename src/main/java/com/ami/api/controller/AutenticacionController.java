package com.ami.api.controller;

import com.ami.api.infra.auditoria.AuditLogService;
import jakarta.validation.Valid;
import com.ami.api.domain.usuario.DatosAutenticacion;
import com.ami.api.domain.usuario.Usuario;
import com.ami.api.infra.security.DatosTokenJwt;
import com.ami.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity iniciarSesion(@RequestBody @Valid DatosAutenticacion datos) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(datos.email(), datos.password());//Saber datos del usuario que esta intentando loguearse
        var autenticacion = authenticationManager.authenticate(authenticationToken);

        var tokenJwt = tokenService.generarToken((Usuario) autenticacion.getPrincipal());

        // Audit log
        auditLogService.registrarLog(
                datos.email(),
                "AUTH",
                "Inicio de sesión exitoso y token JWT generado"
        );

        return ResponseEntity.ok(new DatosTokenJwt(tokenJwt));//Pasarle el usuario actual
    }
}
