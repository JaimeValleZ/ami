package com.ami.api.domain.consulta.validaciones;

import com.ami.api.domain.ValidacionException;
import com.ami.api.domain.consulta.DatosReservaConsulta;
import com.ami.api.domain.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacionConsultaMedicoInactivo implements ValidacionDeConsultas{

    @Autowired
    private MedicoRepository medicoRepository;

    public void validar(DatosReservaConsulta datos){

        //Eleccion del medico opcional
        if(datos.idMedico() == null){
            return;
        }

        var medicoActivoOnO = medicoRepository.findActivoById(datos.idMedico());

        if(!medicoActivoOnO){
            throw new ValidacionException("El medico esta inactivo");
        }
    }
}
