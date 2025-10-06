package com.ami.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.ami.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component //Algo generico que Spring debe cargar
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("ENTRADA FULTRO");
        var tokenJWT = recuperarToken(request);//Traer token del usuario
        //Si se viene el token se hace la siguiente autenticacion
        if(tokenJWT != null){
            var subject = tokenService.getEmail(tokenJWT);//Verificar el token y obtener el usuario
            var usuario = usuarioRepository.findByEmail(subject);

            //Metodo de spring para saber cual es el usuario autenticado y decirle que el usuario ya etsa logueado
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("USUARIO AUTENTICADO");

        }
        //Si no viene token, spring se encarga de la autenticacion (en caso de que sea para rutas publicas)
        filterChain.doFilter(request, response);//Seguir con la cadena de filtros para poder llegar al controller
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null){
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
