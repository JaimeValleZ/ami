package com.ami.api.domain.paciente;

public record DatosListarPacientesDTO(
        Long id,
        String nombre,
        String email,
        String documento
) {

    public DatosListarPacientesDTO(Paciente paciente) {
        this(paciente.getId(), paciente.getNombre(), paciente.getEmail(), paciente.getDocumento());
    }
}
