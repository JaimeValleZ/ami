package com.ami.api.domain.medico;

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
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuditLogService auditLogService;

    public Medico registrarMedico(DatosRegistroMedico datos) {
        // Crear usuario vinculado
        Usuario usuario = new Usuario();
        usuario.setEmail(datos.email());
        usuario.setPassword(passwordEncoder.encode(datos.password()));

        // Asignar rol de MEDICO
        Rol rolMedico = rolRepository.findByNombre("MEDICO")
                .orElseThrow(() -> new RuntimeException("Rol MEDICO no encontrado"));
        usuario.getRoles().add(rolMedico);

        usuarioRepository.save(usuario);

        // Crear médico asociado al usuario
        Medico medico = new Medico(datos, usuario);

        // Obtener usuario que ejecuta la acción (el admin autenticado)
        String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // Registrar log
        auditLogService.registrarLog(
                adminUsername,
                "CREAR_MEDICO",
                "El usuario " + adminUsername + " registró al médico con id " + medico.getId()
        );
        return medicoRepository.save(medico);
    }

    public Page<DatosListaMedico> listarMedicos(Pageable paginacion) {
        return medicoRepository.findAllByActivoTrue(paginacion).map(DatosListaMedico::new);
    }

    public Medico actualizarMedico(DatosActualizacionMedico datos) {
        var medico = medicoRepository.getReferenceById(datos.id());
        medico.actualizarInformaciones(datos);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogService.registrarLog(
                username,
                "ACTUALIZAR_MEDICO",
                "El usuario " + username + " actualizó al médico con id " + medico.getId()
        );
        return medico;
    }

    public void eliminarMedico(Long id) {
        var medico = medicoRepository.getReferenceById(id);
        medico.eliminar();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        auditLogService.registrarLog(
                username,
                "ELIMINAR_MEDICO",
                "El usuario " + username + " eliminó (lógicamente) al médico con id " + id
        );
    }

    public Medico detallarMedico(Long id) {
        return medicoRepository.getReferenceById(id);
    }
}
