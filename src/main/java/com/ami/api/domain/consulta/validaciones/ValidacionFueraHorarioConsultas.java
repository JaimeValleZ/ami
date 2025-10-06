package com.ami.api.domain.consulta.validaciones;

import com.ami.api.domain.ValidacionException;
import com.ami.api.domain.consulta.DatosReservaConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidacionFueraHorarioConsultas implements ValidacionDeConsultas{

    public void validar(DatosReservaConsulta datos) {
        var fechaConsulta = datos.fecha();
        var domingo = fechaConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var horarioAntesDeApertura = fechaConsulta.getHour() < 7;
        var horarioDespuesDeCierre = fechaConsulta.getHour() > 18;
        if(domingo || horarioAntesDeApertura || horarioDespuesDeCierre){
            throw new ValidacionException("Horario seleccionado fuera del horario de atencion de la clinica");
        }
    }
}
