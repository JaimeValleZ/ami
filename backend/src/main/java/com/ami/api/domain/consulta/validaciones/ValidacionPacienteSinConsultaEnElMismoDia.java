package com.ami.api.domain.consulta.validaciones;

import com.ami.api.domain.consulta.ConsultaRepository;
import com.ami.api.domain.consulta.DatosReservaConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacionPacienteSinConsultaEnElMismoDia implements ValidacionDeConsultas{

    @Autowired
    private ConsultaRepository repository;

    public void validar(DatosReservaConsulta datos){
        var primerHorario = datos.fecha().withHour(7);
        var ultimoHorario = datos.fecha().withHour(18);
        var pacienteTieneConsultaEnElDia = repository.existsByPacienteIdAndFechaBetween(datos.idPaciente(), primerHorario, ultimoHorario);
        if(pacienteTieneConsultaEnElDia){
            throw new RuntimeException("El paciente ya tiene una consulta en el mismo dia");
        }

    }
}
