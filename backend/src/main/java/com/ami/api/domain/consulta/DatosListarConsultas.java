package com.ami.api.domain.consulta;

import com.ami.api.domain.medico.Especialidad;

import java.time.LocalDateTime;

public record DatosListarConsultas(
        Long idConsulta,
        Long idMedico,
        Long idPaciente,
        LocalDateTime fecha,
        Especialidad especialidad,
        MotivoCancelamiento motivoCancelamiento
) {
    public DatosListarConsultas(Consulta consulta){
        this(consulta.getId(), consulta.getMedico().getId(), consulta.getPaciente().getId(), consulta.getFecha(), consulta.getMedico().getEspecialidad(), consulta.getMotivoCancelamiento());
    }
}
