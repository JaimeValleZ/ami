package com.ami.api.domain.paciente;

import com.ami.api.domain.direccion.Direccion;

public record DatosDetallePaciente(
        Long id,
        String nombre,
        String telefon,
        String email,
        String documento,
        String telefono,
        Direccion direccion
) {
    public DatosDetallePaciente(Paciente paciente) {
        this(
                paciente.getId(), paciente.getNombre(), paciente.getTelefono(), paciente.getEmail(),
                paciente.getDocumento(), paciente.getTelefono(), paciente.getDireccion()
        );
    }
}
