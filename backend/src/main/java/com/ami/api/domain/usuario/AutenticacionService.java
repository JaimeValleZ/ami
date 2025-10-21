package com.ami.api.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionService implements UserDetailsService {

    @Autowired
    UsuarioRepository repository;

    //La interfaz UserDetailsService permite cargar el usuario que se esta logueando y buscarlo en la base de datos
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = (Usuario) repository.findByEmail(username);
        if (usuario == null || !usuario.getActivo()) {
            throw new UsernameNotFoundException("Usuario bloqueado o no encontrado");
        }
        return usuario;
    }
}
