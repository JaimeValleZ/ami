package com.ami.api.domain.paciente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import com.ami.api.domain.direccion.DatosDireccion;

public record DatosRegistroPacienteDTO(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        @NotBlank String telefono,
        @NotBlank @Pattern(regexp = "\\d{7,12}") String documento,
        @NotNull @Valid DatosDireccion direccion,
        @NotBlank String password
) {
}
