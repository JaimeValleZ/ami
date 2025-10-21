package com.ami.api.domain.consulta.validaciones;

import com.ami.api.domain.ValidacionException;
import com.ami.api.domain.consulta.DatosReservaConsulta;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidacionConsultaConAnticipacion implements ValidacionDeConsultas{

    public void validar(DatosReservaConsulta datos) {
        var ahora = LocalDateTime.now();
        var fechaConsulta = datos.fecha();
        var diferenciaEnMinutos = Duration.between(ahora, fechaConsulta).toMinutes();
        if(diferenciaEnMinutos < 30){
            throw new ValidacionException("La consulta debe ser realizada con anticipacion de 30 minutos o mas");
        }
    }
}
