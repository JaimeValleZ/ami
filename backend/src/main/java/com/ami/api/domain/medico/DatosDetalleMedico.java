package com.ami.api.domain.medico;

import com.ami.api.domain.direccion.Direccion;

public record DatosDetalleMedico(
        Long id,
        String nombre,
        String telefon,
        String email,
        String documento,
        String telefono,
        Especialidad especialidad,
        Direccion direccion
) {
    public DatosDetalleMedico(Medico medico) {
        this(
                medico.getId(), medico.getNombre(), medico.getTelefono(), medico.getEmail(),
                medico.getDocumento(), medico.getTelefono(), medico.getEspecialidad(),
                medico.getDireccion()
        );
    }
}
