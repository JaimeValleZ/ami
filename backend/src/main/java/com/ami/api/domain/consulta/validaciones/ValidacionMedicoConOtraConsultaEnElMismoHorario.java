package com.ami.api.domain.consulta.validaciones;

import com.ami.api.domain.consulta.ConsultaRepository;
import com.ami.api.domain.consulta.DatosReservaConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacionMedicoConOtraConsultaEnElMismoHorario implements ValidacionDeConsultas{

    @Autowired
    private ConsultaRepository repository;

    public void validar(DatosReservaConsulta datos){
        var medicoTieneOtraConsultaEnElMismoDia = repository.existsByMedicoIdAndFecha(datos.idMedico(), datos.fecha());
        if(medicoTieneOtraConsultaEnElMismoDia){
            throw new RuntimeException("El medico ya tiene una consulta en esa misma fecha y hora");
        }
    }
}
