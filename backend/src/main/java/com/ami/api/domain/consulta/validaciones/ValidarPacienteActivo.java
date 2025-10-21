package com.ami.api.domain.consulta.validaciones;

import com.ami.api.domain.consulta.DatosReservaConsulta;
import com.ami.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarPacienteActivo implements ValidacionDeConsultas{

    @Autowired
    private PacienteRepository repository;

    public void validar(DatosReservaConsulta datos){
        var pacienteActivoONo = repository.findActivoById(datos.idPaciente());
        if(!pacienteActivoONo){
            throw new RuntimeException("El paciente con el id " + datos.idPaciente() + " esta inactivo");
        }
    }
}
