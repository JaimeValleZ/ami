package com.ami.api.infra.security;

import com.ami.api.domain.medico.MedicoRepository;
import com.ami.api.domain.paciente.PacienteRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ami.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")//Obtener del .properties
    private String secret;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    public String generarToken(Usuario usuario){
        try {
            var algoritmo = Algorithm.HMAC256(secret); //RSA es algoritmo de generacion de token
            String nombre = obtenerNombrePorUsuario(usuario);

            return JWT.create()
                    .withIssuer("API Voll.med") //Decir el servidor que firma el token
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId())
                    .withClaim("roles", usuario.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList())
                    .withClaim("nombre", nombre)
                    .withExpiresAt(fechaExpiracion())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error al generar el token", exception);
        }
    }

    private String obtenerNombrePorUsuario(Usuario usuario) {
        // Intentar obtener nombre desde Paciente o Medico según corresponda
        if (pacienteRepository != null) {
            var paciente = pacienteRepository.findByUsuarioEmail(usuario.getEmail());
            if (paciente != null) {
                return paciente.getNombre();
            }
        }
        if (medicoRepository != null) {
            var medico = medicoRepository.findByUsuarioEmail(usuario.getEmail());
            if (medico != null) {
                return medico.getNombre();
            }
        }
        // Por defecto, devolver el email si no se encuentra nombre
        return usuario.getEmail();
    }

    private Instant fechaExpiracion() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));//Horas del meriadirio de wreenchich
    }

    public String getEmail(String tokenJWT){
        try{
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API Voll.med")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();//RSA es algoritmo de generacion de token
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT invalido o expirado");
        }
    }

}
