package com.ami.api.domain.consulta.validaciones;

import com.ami.api.domain.consulta.ConsultaRepository;
import com.ami.api.domain.consulta.DatosCancelamientoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidacionCancelarCita24H{

    @Autowired
    private ConsultaRepository repository;

    public void validar(DatosCancelamientoConsulta datos) {
        var consulta = repository.findById(datos.idConsulta()).orElseThrow(null);
        var fechaAhora = LocalDateTime.now();
        var fechaCita = consulta.getFecha();
        var diferenciaEnHoras = Duration.between(fechaAhora, fechaCita).toHours();
        if(diferenciaEnHoras < 24){
            throw new RuntimeException("Las citas deben cancelarse 24 horas antes de la fecha de la consulta");
        }
    }
}
