package com.ami.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean//La clase tiene que estar cargada y el metodo disponnile para que spring security lo pueda leer
    public SecurityFilterChain securityFilterChain(HttpSecurity http, com.ami.api.infra.security.CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {
        return http.csrf(csrf -> csrf.disable()) //sistema de cookies por el cual podemos recibir ataques
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//Sistema Statelles y no mas reenvio al formulario de liogin
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, "/login").permitAll()
                            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                            // Consultas
                            .requestMatchers("/consultas/**").hasRole("PACIENTE")

                            // PacienteController
                            .requestMatchers(HttpMethod.GET,"/paciente/**").hasAnyRole("PACIENTE", "ADMIN", "MEDICO")
                            .requestMatchers(HttpMethod.DELETE,"/paciente/**").hasAnyRole("PACIENTE", "ADMIN")
                            .requestMatchers(HttpMethod.POST,"/paciente").permitAll()
                            .requestMatchers(HttpMethod.PUT,"/paciente/**").hasAnyRole("PACIENTE", "ADMIN")

                            // MedicoController
                            .requestMatchers(HttpMethod.POST,"/medicos").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE,"/medicos/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT,"/medicos/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET,"/medicos/**").hasAnyRole("ADMIN", "MEDICO")

                            .anyRequest().authenticated();
                })
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(customAccessDeniedHandler) // handler de acceso
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)//Decirle a spring que ejecute primero nuestro filtro y luego a el generico de spring
                .build();

    }

    //Construir objeto authentication manager para usarlo en otas partes
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
