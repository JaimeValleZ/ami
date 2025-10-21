package com.ami.api.controller;

import com.ami.api.domain.consulta.DatosListarConsultas;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import com.ami.api.domain.consulta.DatosCancelamientoConsulta;
import com.ami.api.domain.consulta.DatosReservaConsulta;
import com.ami.api.domain.consulta.ReservaDeConsultas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaController {

    @Autowired
    private ReservaDeConsultas reservaDeConsultas;

    @PostMapping
    @Transactional
    public ResponseEntity reservar(@RequestBody @Valid DatosReservaConsulta datos){

        var detalleConsulta = reservaDeConsultas.reservar(datos);

        return ResponseEntity.ok(detalleConsulta);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity cancelar(@RequestBody @Valid DatosCancelamientoConsulta datos) {
        reservaDeConsultas.cancelar(datos);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/consultas/{idPaciente}")
    public ResponseEntity<Page<DatosListarConsultas>> listarConsultas(
            @PathVariable Long idPaciente,
            @PageableDefault(size = 10, sort = {"fecha"}, direction = Sort.Direction.DESC)Pageable paginacion
            ) {
        var page = reservaDeConsultas.listarConsultas(paginacion, idPaciente);
        return ResponseEntity.ok(page);
    }
}
