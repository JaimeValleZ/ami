package com.ami.api.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import com.ami.api.domain.ValidacionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;


@RestControllerAdvice //Anotacion para gestion de errores
public class GestorDeErrores {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity gestionarError404(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity gestionarError400(MethodArgumentNotValidException mensaje){
        var errores = mensaje.getFieldErrors(); //Obtener todos los errores dentro de una variable, PERMITE devolver el campo donde esta el error y cual es el problema
        return ResponseEntity.badRequest().body(errores.stream().map(DatosErrorValidacion::new).toList());
    }

    public record DatosErrorValidacion(String campo, String mensaje){
        public DatosErrorValidacion(FieldError error){
            this(error.getField(), error.getDefaultMessage());
        }
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity gestionarError(DataIntegrityViolationException mensaje){
        var errores = mensaje.getMessage();
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity gestionarErrorBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity gestionarErrorAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falla en la autenticación");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity gestionarErrorAccesoDenegado() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity gestionarError500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " +ex.getLocalizedMessage());
    }

    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity gestionarErrorValidacion(ValidacionException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
