package com.ami.api.domain.medico;

import jakarta.validation.constraints.NotNull;
import com.ami.api.domain.direccion.DatosDireccion;

public record DatosActualizacionMedico(
        @NotNull Long id,
        String nombre,
        String telefono,
        DatosDireccion direccion
) {
}
