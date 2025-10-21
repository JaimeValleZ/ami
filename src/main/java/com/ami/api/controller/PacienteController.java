package com.ami.api.controller;

import com.ami.api.domain.paciente.*;
import com.ami.api.domain.paciente.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/paciente")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin("*")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Transactional
    @PostMapping
    public ResponseEntity registrarPaciente(@RequestBody @Valid DatosRegistroPacienteDTO datos,
                                            UriComponentsBuilder uriComponentsBuilder) {
        var paciente = pacienteService.registrarPaciente(datos);
        var uri = uriComponentsBuilder.path("/paciente/{id}")
                .buildAndExpand(paciente.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new DatosDetallePaciente(paciente));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListarPacientesDTO>> listarPacientes(
            @PageableDefault(size = 10, sort = {"nombre"}) Pageable paginacion) {
        var page = pacienteService.listarPacientes(paginacion);
        return ResponseEntity.ok(page);
    }

    @Transactional
    @PutMapping
    public ResponseEntity actualizarPaciente(@RequestBody @Valid DatosActualizacionPacienteDTO datos) {
        var paciente = pacienteService.actualizarPaciente(datos);
        return ResponseEntity.ok(new DatosDetallePaciente(paciente));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity bloquearPaciente(@PathVariable Long id) {
        pacienteService.bloquearPaciente(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity detallarPaciente(@PathVariable Long id) {
        var paciente = pacienteService.detallarPaciente(id);
        return ResponseEntity.ok(new DatosDetallePaciente(paciente));
    }
}
