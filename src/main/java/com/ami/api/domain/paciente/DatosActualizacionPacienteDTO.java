package com.ami.api.domain.paciente;

import jakarta.validation.constraints.NotNull;
import com.ami.api.domain.direccion.DatosDireccion;

public record DatosActualizacionPacienteDTO(
        @NotNull Long id,
        String nombre,
        String telefono,
        DatosDireccion direccion
) {
}
