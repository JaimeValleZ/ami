package com.ami.api.domain.paciente;

import com.ami.api.domain.usuario.Usuario;
import com.ami.api.domain.usuario.UsuarioRepository;
import com.ami.api.domain.usuario.Rol;
import com.ami.api.domain.usuario.RolRepository;
import com.ami.api.infra.auditoria.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuditLogService auditLogService;

    public Paciente registrarPaciente(DatosRegistroPacienteDTO datos) {
        // Crear usuario vinculado
        Usuario usuario = new Usuario();
        usuario.setEmail(datos.email());
        usuario.setPassword(passwordEncoder.encode(datos.password()));

        // Asignar rol de PACIENTE
        Rol rolPaciente = rolRepository.findByNombre("PACIENTE")
                .orElseThrow(() -> new RuntimeException("Rol PACIENTE no encontrado"));
        usuario.getRoles().add(rolPaciente);

        usuarioRepository.save(usuario);

        // Crear paciente asociado al usuario
        Paciente paciente = new Paciente(datos, usuario);

        // Usuario autenticado que registró al paciente (puede ser admin o self-register)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogService.registrarLog(
                username,
                "CREAR_PACIENTE",
                "El usuario " + username + " registró al paciente con id " + paciente.getId()
        );

        return pacienteRepository.save(paciente);
    }

    public Page<DatosListarPacientesDTO> listarPacientes(Pageable paginacion) {
        return pacienteRepository.findAllByActivoTrue(paginacion).map(DatosListarPacientesDTO::new);
    }

    public Paciente actualizarPaciente(DatosActualizacionPacienteDTO datos) {
        var paciente = pacienteRepository.getReferenceById(datos.id());
        paciente.actualizarPaciente(datos);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogService.registrarLog(
                username,
                "ACTUALIZAR_PACIENTE",
                "El usuario " + username + " actualizó su información (paciente id " + paciente.getId() + ")"
        );
        return paciente;
    }

    public void bloquearPaciente(Long id) {
        var paciente = pacienteRepository.getReferenceById(id);
        paciente.bloquearPaciente();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogService.registrarLog(
                username,
                "BLOQUEAR_PACIENTE",
                "El administrador " + username + " bloqueó al paciente con id " + id
        );
    }

    public Paciente detallarPaciente(Long id) {
        return pacienteRepository.getReferenceById(id);
    }
}
