package com.ami.api.controller;

import com.ami.api.domain.medico.*;
import com.ami.api.domain.medico.*;
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
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @Transactional
    @PostMapping
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroMedico datos,
                                    UriComponentsBuilder uriComponentsBuilder) {
        var medico = medicoService.registrarMedico(datos);
        var uri = uriComponentsBuilder.path("/medicos/{id}")
                .buildAndExpand(medico.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new DatosDetalleMedico(medico));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListaMedico>> listarMedicos(
            @PageableDefault(size = 10, sort = {"nombre"}) Pageable paginacion) {
        var page = medicoService.listarMedicos(paginacion);
        return ResponseEntity.ok(page);
    }

    @Transactional
    @PutMapping
    public ResponseEntity actualizar(@RequestBody @Valid DatosActualizacionMedico datos) {
        var medico = medicoService.actualizarMedico(datos);
        return ResponseEntity.ok(new DatosDetalleMedico(medico));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(@PathVariable Long id) {
        medicoService.eliminarMedico(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detallarUsuario(@PathVariable Long id) {
        var medico = medicoService.detallarMedico(id);
        return ResponseEntity.ok(new DatosDetalleMedico(medico));
    }
}
